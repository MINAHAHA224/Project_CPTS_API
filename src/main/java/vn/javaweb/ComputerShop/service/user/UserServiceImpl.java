package vn.javaweb.ComputerShop.service.user;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.javaweb.ComputerShop.component.GoogleOauth2;
import vn.javaweb.ComputerShop.component.JwtUtils;
import vn.javaweb.ComputerShop.component.MailerComponent;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.domain.entity.*;
import vn.javaweb.ComputerShop.domain.enums.CartStatus;
import vn.javaweb.ComputerShop.handleException.AuthException;
import vn.javaweb.ComputerShop.handleException.BusinessException;
import vn.javaweb.ComputerShop.repository.auth.AuthMethodRepository;
import vn.javaweb.ComputerShop.repository.cart.CartRepository;
import vn.javaweb.ComputerShop.repository.user.RoleRepository;
import vn.javaweb.ComputerShop.repository.user.UserOtpRepository;
import vn.javaweb.ComputerShop.repository.user.UserRepository;
import vn.javaweb.ComputerShop.service.upload.UploadService;
import vn.javaweb.ComputerShop.utils.SecurityUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthMethodRepository authMethodRepository;
    private final UserOtpRepository userOtpRepository;
    private final MailerComponent mailerComponent;
    private final UploadService uploadService;
    private final MessageComponent messageComponent;
    private final JwtUtils jwtUtils;


    private final RestTemplate restTemplate = new RestTemplate();
    private final GoogleOauth2 googleOauth2;


    @Value("${spring.security.oauth2.client.registration.google.client-id}") // Lấy client ID từ properties
    private String GOOGLE_CLIENT_ID;

    @Override
    public InformationDTO handleLogin(LoginDTO loginDTO, Locale locale) {

        String email = loginDTO.getEmail().trim();
        String password = loginDTO.getPassword().trim();
        UserEntity user = new UserEntity();
        Optional<UserEntity> emailOnDb = this.userRepository.findUserEntityByEmail(email);
        if (emailOnDb.isPresent()) {
            user = emailOnDb.get();
        } else {
            throw new AuthException(messageComponent.getLocalizedMessage("user.login.error.emailNotRegistered", locale));

        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException( messageComponent.getLocalizedMessage("user.login.error.incorrectPassword", locale));
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password,
                user.getAuthorities()
        );

        try{
            authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            log.warn("Authentication failed in authenticationManager layering: {} ", e.getMessage());
            throw new AuthException(messageComponent.getLocalizedMessage("user.login.error.authenticationFailed", locale));
        }

        // generate TokenJWT for user
        String token = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().getName());

        return InformationDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .role(user.getRole().getName())
                .tokenJWT(token)
                .build();
    }

    @Override
    public InformationDTO handleRegister(RegisterDTO registerDTO, Locale locale) {
        UserEntity user = new UserEntity();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        RoleEntity role = this.roleRepository.findRoleEntityByName("USER");
        user.setRole(role);
        this.userRepository.save(user);
        return InformationDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getName())
                .avatar(user.getAvatar())
                .build();

    }

    @Override
    public String handleRedirectToGoogle() {
        return googleOauth2.getAuthUrl();
    }

    @Override
    @Transactional
    public InformationDTO handleLoginOauth2Google(String code, Locale locale) {
        InformationDTO informationDTO = new InformationDTO();
        // 1. Get access token
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(
                googleOauth2.buildTokenRequestBody(code),
                googleOauth2.getHeadersForToken()
        );

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                googleOauth2.getTokenEndpoint(), tokenRequest, Map.class);

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        //Call userinfo endpoint , google need bearer not bearer for user use to connect api to software
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                googleOauth2.getUserInfoEndpoint(), HttpMethod.GET, entity, Map.class);

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String external_id = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");

        boolean existEmail = this.userRepository.existsByEmail(email);
        // if exists => create token for security
        if (existEmail) {
            UserEntity user = this.userRepository.findUserEntityByEmail(email)
                    .orElseThrow(() -> new AuthException("User not found"));
            informationDTO.setId(user.getId());
            informationDTO.setEmail(user.getEmail());
            informationDTO.setFullName(user.getFullName());
            informationDTO.setAvatar(user.getAvatar());
            informationDTO.setRole(user.getRole().getName());


            Authentication BybassAuthenticationForLoginGoogle = new UsernamePasswordAuthenticationToken(email, null, user.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(BybassAuthenticationForLoginGoogle);
            SecurityContextHolder.setContext(context);
        } else {
            // save user first
            UserEntity userNew = new UserEntity();
            userNew.setEmail(email);
            userNew.setFullName(name);
            RoleEntity role = this.roleRepository.findRoleEntityByName("USER");
            userNew.setRole(role);
            UserEntity userCurrent = this.userRepository.save(userNew);

            // save AuthMethod second
            AuthMethodEntity authMethod = new AuthMethodEntity();
            authMethod.setUser(userCurrent);
            authMethod.setLogin_type("GOOGLE");
            authMethod.setExternal_id(external_id);
            this.authMethodRepository.save(authMethod);

            UserEntity user = this.userRepository.findUserEntityByEmail(email).orElseThrow(
                    () -> new AuthException("User not found")
            );
            informationDTO.setId(user.getId());
            informationDTO.setEmail(email);
            informationDTO.setFullName(name);
            informationDTO.setAvatar(picture);
            informationDTO.setRole(user.getRole().getName());
        }
        // generate TokenJWT for user
        String tokenJWT = jwtUtils.generateToken(informationDTO.getId(), informationDTO.getEmail(), informationDTO.getRole());
        informationDTO.setTokenJWT(tokenJWT);
        return informationDTO;
    }

    @Override
    public String handleSendOTP(String email, Locale locale) {
        UserEntity user = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new BusinessException(messageComponent.getLocalizedMessage("user.otp.error.emailNotRegisteredOrError", locale)));
        // second check email has OTP not yet expired , if it has , can not canSentEmail = false
        boolean canSentEmail = true;
        List<UserOtpEntity> listUserOtp = this.userOtpRepository.findUserOtpEntityByUser(user);
        for (UserOtpEntity userOtp : listUserOtp) {
            if (userOtp.getExpiredTime().isAfter(LocalDateTime.now())) {
                canSentEmail = false;
                break;
            }
        }
        if (canSentEmail) {
                String OTP = this.mailerComponent.generateOTP(6);
                // Use multiple thread to increase speed sendOTP
                // Use Async in Spring to handle this feature
                this.mailerComponent.handleSendConfirmLink(email, OTP);

                UserOtpEntity userOtp = new UserOtpEntity();
                userOtp.setUser(user);
                userOtp.setOtpCode(OTP);
                userOtp.setCreatedAt(LocalDateTime.now());
                userOtp.setExpiredTime(LocalDateTime.now().plusMinutes(1));
                userOtp.setUsed(false);
                // set OTP to database
                this.userOtpRepository.save(userOtp);
                // set body
                return OTP;
        } else {
            throw new BusinessException( messageComponent.getLocalizedMessage("user.otp.error.alreadySentActive", locale));
        }
    }

    @Override
    @Transactional
    public void handleVerifyOTP(String email, String OTP, Locale locale) {
        UserEntity user = this.userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new BusinessException(messageComponent.getLocalizedMessage("user.otp.error.emailNotRegisteredOrError", locale)));
            // check first email have OTP not yet Expired if has userOtpEnough = have data  otherwise has no data
            List<UserOtpEntity> listUserOtp = this.userOtpRepository.findUserOtpEntityByUser(user);
            UserOtpEntity userOtpEnough = new UserOtpEntity();
            for (UserOtpEntity userOtp : listUserOtp) {
                if (userOtp.getExpiredTime().isAfter(LocalDateTime.now())) {
                    userOtpEnough = userOtp;
                    break;
                }
            }

            // if has userOtpEnough = have data , accept and update userOtp
            if (userOtpEnough.getId() != null) {
                boolean used = userOtpEnough.isUsed();
                String otpDb = userOtpEnough.getOtpCode();

                if (otpDb.equals(OTP) && !used) {
                    // Update OTP is used

                        userOtpEnough.setUsed(true);
                        this.userOtpRepository.save(userOtpEnough);



                } else {
                    throw new BusinessException(messageComponent.getLocalizedMessage("user.otp.error.invalidOrExpiredOrUsed", locale));
                }
                //  otherwise has no data , announcement error
            } else {
                throw new BusinessException(messageComponent.getLocalizedMessage("user.otp.error.invalidOrExpiredOrUsed", locale));
            }

    }

    @Override
    public void handleResetPassword(ResetPasswordDTO resetPasswordDTO, Locale locale) {
        String email = resetPasswordDTO.getEmail().trim();
            UserEntity user = this.userRepository.findUserEntityByEmail(email).
                    orElseThrow(() -> new AuthException("User not found"));
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            this.userRepository.save(user);
    }

    @Override
    public List<UserRpDTO> handleGetUsers() {
        List<UserEntity> listEntity = this.userRepository.findAll();
        return listEntity.stream().map(us ->
                UserRpDTO.builder()
                        .id(us.getId())
                        .email(us.getEmail())
                        .fullName(us.getFullName())
                        .nameRole(us.getRole().getName())
                        .build()
        ).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public ApiResponse handleCreateUser(UserCreateRqDTO userCreateRqDTO, MultipartFile file) {

        String email = userCreateRqDTO.getEmail().trim();
        String address = userCreateRqDTO.getAddress().trim();
        String phone = userCreateRqDTO.getPhone().trim();
        String fullName = userCreateRqDTO.getFullName().trim();
        String avatar = this.uploadService.handleUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(userCreateRqDTO.getPassword());
        RoleEntity role = this.roleRepository.findRoleEntityByName(userCreateRqDTO.getRoleName());
        // handle check email and password
        boolean checkEmailExist = this.userRepository.existsUserEntityByEmail(email);
        if (checkEmailExist) {
            return new ApiResponse(500, "Admin : email đã có tài khoản sử dụng");
        }
        boolean checkExistPhone = this.userRepository.existsUserEntityByPhone(phone);
        if (checkExistPhone) {
            return new ApiResponse(500, "Admin : Số tài khoản đã được sử dụng");
        }
        // save user
        UserEntity user = UserEntity.builder()
                .email(email)
                .address(address)
                .phone(phone)
                .fullName(fullName)
                .avatar(avatar)
                .password(hashPassword)
                .role(role)
                .build();
        this.userRepository.save(user);
        return new ApiResponse(200, "Admin : tạo tài khoản thành công");

    }

    @Override
    public UserDetailDTO handleGetUserDetail(Long id) {
        UserEntity user = this.userRepository.findUserEntityById(id);
        if (user == null) {
            throw new AuthException("User not found");
        }
        return UserDetailDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roleName(user.getRole().getName())
                .avatar(user.getAvatar())
                .build();
    }


    @Override
    public UserUpdateRqDTO handleShowDataUserUpdate(Long id) {
        UserEntity user = this.userRepository.findUserEntityById(id);
        if (user == null) {
            throw new AuthException("User not found");
        }
        return UserUpdateRqDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roleName(user.getRole().getName())
                .avatar(user.getAvatar())
                .build();
    }


    @Override
    @Transactional
    public ApiResponse handleUpdateUser(UserUpdateRqDTO userUpdateRqDTO, MultipartFile file) {
        UserEntity userCurrent = this.userRepository.findUserEntityById(userUpdateRqDTO.getId());
        if (userCurrent == null) {
            throw new AuthException("User not found");
        }

        RoleEntity role = this.roleRepository.findRoleEntityByName(userUpdateRqDTO.getRoleName().trim());
        String phone = userUpdateRqDTO.getPhone().trim();

        // handle check phone
        boolean checkExistPhone = this.userRepository.existsUserEntityByPhone(phone);
        if (checkExistPhone) {
            return new ApiResponse(500, "Admin : Số điện thoại đã được sử dụng");
        }
        // set data new
        userCurrent.setFullName(userUpdateRqDTO.getFullName());
        userCurrent.setAddress(userUpdateRqDTO.getAddress());
        userCurrent.setPhone(userUpdateRqDTO.getPhone());
        userCurrent.setRole(role);
        if (file != null && !Objects.equals(file.getOriginalFilename(), "")) {
            String newAvatar = this.uploadService.handleUploadFile(file, "avatar");
            userCurrent.setAvatar(newAvatar);
        }
        this.userRepository.save(userCurrent);
        return new ApiResponse(200, "Admin : Cập nhật tài khoản người dùng thành công");
    }


    @Override
    @Transactional
    public ApiResponse handleDeleteUser(Long id) {
        UserEntity user = this.userRepository.findUserEntityById(id);
        if (user != null) {
            throw new AuthException("User not found");
        }
        this.userRepository.deleteUserEntityById(id);
        return new ApiResponse(200, "Admin : Xóa tài khoản thành công");
    }

    @Override
    public UserProfileUpdateDTO handleGetDataUserToProfile() {
        String email = SecurityUtils.getPrincipal();
        UserEntity userEntity = this.userRepository.findUserEntityByEmail(email).orElseThrow(
                () -> new AuthException("User not found")
        );
        boolean checkOauth2 = this.authMethodRepository.existsAuthMethodEntityByUser(userEntity);

        return UserProfileUpdateDTO.builder()
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .address(userEntity.getAddress())
                .avatar(userEntity.getAvatar())
                .phone(userEntity.getPhone())
                .hasChangePass(!checkOauth2)
                .build();
    }


    @Override
    @Transactional
    public void handleUpdateProfile(UserProfileUpdateDTO userProfileUpdateDTO, Locale locale) {
        UserEntity user = this.userRepository.findUserEntityByEmail(SecurityUtils.getPrincipal()).orElseThrow(
                () -> new AuthException("User not found")
        );
        boolean checkPhone = this.userRepository.existsUserEntityByPhone(userProfileUpdateDTO.getPhone().trim());

        if (user.getPhone() == null || !user.getPhone().equals(userProfileUpdateDTO.getPhone())) {
            if (checkPhone) {
                throw  new BusinessException(messageComponent.getLocalizedMessage("user.profile.update.error.phoneExists", locale) );
            }
            user.setPhone(userProfileUpdateDTO.getPhone().trim());
        }

        user.setAddress(userProfileUpdateDTO.getAddress().trim());
        this.userRepository.save(user);

    }


    @Override
    @Transactional
    public void handleUpdateAvatar(MultipartFile avatarFile, Locale locale) {

        UserEntity user = this.userRepository.findUserEntityByEmail(SecurityUtils.getPrincipal()).orElseThrow(
                () -> new AuthException("User not found")
        );

        if (Objects.equals(avatarFile.getOriginalFilename(), "") || avatarFile.isEmpty()) {
            throw new BusinessException( messageComponent.getLocalizedMessage("avatar.update.error.emptyFile", locale));
        }

        String avatarNew = this.uploadService.handleUploadFile(avatarFile, "avatar");
        user.setAvatar(avatarNew);
        this.userRepository.save(user);

    }


    @Override
    @Transactional
    public void handleUpdatePassword(ChangePasswordDTO changePasswordDTO, Locale locale) {
        UserEntity user = this.userRepository.findUserEntityByEmail(SecurityUtils.getPrincipal()).orElseThrow(
                () -> new AuthException("User not found")
        );
        boolean checkPass = this.passwordEncoder.matches(changePasswordDTO.getCurrentPassword().trim(), user.getPassword());
        if (!checkPass) {
            throw new AuthException(messageComponent.getLocalizedMessage("user.password.update.error.newPasswordMismatch" ,locale));
        }
        user.setPassword(this.passwordEncoder.encode(changePasswordDTO.getNewPassword().trim()));
        this.userRepository.save(user);

    }



    @Override
    @Transactional
    public InformationDTO handleLoginWithGoogleIdToken(String idTokenString, Locale locale) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                // Tái sử dụng logic tìm hoặc tạo user
                UserEntity user = findOrCreateUser(email, name, pictureUrl);

                // Đăng nhập thành công, tạo JWT token của hệ thống bạn
                String tokenJWT = jwtUtils.generateToken(user.getId(), user.getEmail(), user.getRole().getName());

                return InformationDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .avatar(user.getAvatar())
                        .role(user.getRole().getName())
                        .tokenJWT(tokenJWT)
                        .build();

            } else {
                throw new AuthException("Invalid Google ID Token.");
            }
        } catch (Exception e) {
            log.error("Error verifying Google ID Token: {}", e.getMessage());
            throw new AuthException("Error verifying Google ID Token.");
        }
    }

    // Tách logic tìm hoặc tạo user ra một hàm riêng để tái sử dụng
    private UserEntity findOrCreateUser(String email, String name, String avatar) {
        Optional<UserEntity> userOptional = userRepository.findUserEntityByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setAvatar(avatar);
            RoleEntity userRole = roleRepository.findRoleEntityByName("USER");
            newUser.setRole(userRole);

            // Lưu AuthMethod để đánh dấu đây là tài khoản Google
            UserEntity savedUser = userRepository.save(newUser);
            AuthMethodEntity authMethod = new AuthMethodEntity();
            authMethod.setUser(savedUser);
            authMethod.setLogin_type("GOOGLE");
            authMethodRepository.save(authMethod);

            return savedUser;
        }
    }


}
