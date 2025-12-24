package vn.javaweb.ComputerShop.service;

import org.springframework.web.multipart.MultipartFile;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.*;

import java.util.Locale;

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



     UserUpdateRqDTO handleShowDataUserUpdate (Long id );
     UserProfileUpdateDTO handleGetDataUserToProfile ();
     void handleUpdateProfile ( UserProfileUpdateDTO userProfileUpdateDTO , Locale locale);
     void handleUpdateAvatar (MultipartFile avatarFile , Locale locale );
     void handleUpdatePassword (ChangePasswordDTO changePasswordDTO , Locale locale);
}