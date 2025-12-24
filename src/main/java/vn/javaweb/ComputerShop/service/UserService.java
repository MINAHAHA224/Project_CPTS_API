package vn.javaweb.ComputerShop.service.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface UserService {

     InformationDTO handleLogin(LoginDTO loginDTO,  Locale locale);
     InformationDTO handleRegister(RegisterDTO registerDTO , Locale  locale);
     InformationDTO handleLoginByFaceId ( FaceDataRequest faceDataRequest, Locale locale);
     void handleUpdateEmbeddingFaceData ( FaceDataRequest faceDataRequest);
     String handleRedirectToGoogle();
     InformationDTO handleLoginOauth2Google(String code, Locale locale);
     String handleSendOTP(String email , Locale locale);
     void handleVerifyOTP(String email, String OTP , Locale locale);
     void handleResetPassword(ResetPasswordDTO resetPasswordDTO , Locale locale);
     InformationDTO handleLoginWithGoogleIdToken (String idTokenString, Locale locale);


     List<UserRpDTO> handleGetUsers();
     ApiResponse handleCreateUser (UserCreateRqDTO userCreateRqDTO , MultipartFile file);
     UserDetailDTO handleGetUserDetail (Long id);
     UserUpdateRqDTO handleShowDataUserUpdate (Long id );
     ApiResponse handleUpdateUser (UserUpdateRqDTO userUpdateRqDTO , MultipartFile file);
     ApiResponse handleDeleteUser (Long id);
     UserProfileUpdateDTO handleGetDataUserToProfile ();
     void handleUpdateProfile ( UserProfileUpdateDTO userProfileUpdateDTO , Locale locale);
     void handleUpdateAvatar (MultipartFile avatarFile , Locale locale );
     void handleUpdatePassword (ChangePasswordDTO changePasswordDTO , Locale locale);
}