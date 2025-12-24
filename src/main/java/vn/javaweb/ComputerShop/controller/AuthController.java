package vn.javaweb.ComputerShop.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.ApiResponseT;
import vn.javaweb.ComputerShop.handleException.exceptions.BusinessException;
import vn.javaweb.ComputerShop.service.UserService;

import java.io.IOException;
import java.util.Locale;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix.auth}")
public class AuthController {
    private final UserService userService;
    private final MessageComponent messageComponent;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseT<InformationDTO>> postLogin(
            Locale locale,
            @Valid @RequestBody LoginDTO loginDTO) {
        InformationDTO informationDTO = this.userService.handleLogin(loginDTO, locale);
        return ResponseEntity.ok().body(ApiResponseT.<InformationDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.login.success", locale))
                .data(informationDTO)
                .build());
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponseT<InformationDTO>> postRegister(
            @Valid @RequestBody RegisterDTO registerDTO
            , Locale locale) {
        InformationDTO handleRegister = this.userService.handleRegister(registerDTO, locale);
        messageComponent.getLocalizedMessage("user.register.success", locale);
        return ResponseEntity.ok().body(ApiResponseT.<InformationDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.register.success", locale))
                .data(handleRegister)
                .build());
    }


    @GetMapping(value = "/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String responseUrl = this.userService.handleRedirectToGoogle();
         response.sendRedirect(responseUrl);
    }

    @GetMapping("/oauth2/code/google")
    public ResponseEntity<ApiResponseT<InformationDTO>>  handleGoogleCallback(@RequestParam("code") String code
            , Locale locale) {
        InformationDTO informationDTO = this.userService.handleLoginOauth2Google(code, locale);
        return ResponseEntity.ok().body(ApiResponseT.<InformationDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.login.success", locale))
                .data(informationDTO)
                .build());

    }

    @PostMapping("/google-signin")
    public ResponseEntity<ApiResponseT<InformationDTO>> handleGoogleSignIn(@RequestBody Map<String, String> payload, Locale locale) {
        String idToken = payload.get("idToken");
        if (idToken == null || idToken.isEmpty()) {
            throw new BusinessException("ID Token is missing.");
        }
        InformationDTO informationDTO = this.userService.handleLoginWithGoogleIdToken(idToken, locale);
        return ResponseEntity.ok().body(ApiResponseT.<InformationDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.login.success", locale))
                .data(informationDTO)
                .build());
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseT<Map<String, String>>> postForgotPassword(
            @RequestParam(name = "email", required = true) String email,
            Locale locale) {
        String otpCode = this.userService.handleSendOTP(email, locale);
        return ResponseEntity.ok().body(ApiResponseT.<Map<String, String>>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.otp.success.sentToEmail", locale))
                .data(Map.of("OTP", otpCode))
                .build());
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseT<Map<String, String>>> postOTPtoVerify(
            @RequestParam(name = "email", required = true) String email
            , @RequestParam(name = "OTP", required = false) String OTP
            , @RequestParam(name = "action", required = true) String action
            , Locale locale) {
        if (action.equals("VERIFY-OTP")) {
            this.userService.handleVerifyOTP(email, OTP, locale);
            return ResponseEntity.ok().body(ApiResponseT.<Map<String, String>>builder()
                    .status(HttpStatus.OK.value())
                    .message(messageComponent.getLocalizedMessage("user.otp.success.verified", locale))
                    .data(Map.of("typeOTP", "VERIFY-OTP"))
                    .build());
        } else {
            String resentOtp = this.userService.handleSendOTP(email.trim(), locale);
            return ResponseEntity.ok().body(ApiResponseT.<Map<String, String>>builder()
                    .status(HttpStatus.OK.value())
                    .message(messageComponent.getLocalizedMessage("user.otp.success.verified", locale))
                    .data(Map.of("typeOTP", "VERIFY-OTP", "OTP", resentOtp))
                    .build());
        }
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<ApiResponseT<?>> postResetPassword(
            Locale locale
            , @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        this.userService.handleResetPassword(resetPasswordDTO, locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.resetPassword.success", locale))
                .build());
    }

    @PostMapping("face-login")
    public ResponseEntity<ApiResponseT<InformationDTO>> postLoginFace(@RequestBody FaceDataRequest faceDataRequest, Locale locale) {
            InformationDTO informationDTO = this.userService.handleLoginByFaceId(faceDataRequest, locale);

        return ResponseEntity.ok().body(ApiResponseT.<InformationDTO>builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.login.success", locale))
                .data(informationDTO)
                .build());
    }
}
