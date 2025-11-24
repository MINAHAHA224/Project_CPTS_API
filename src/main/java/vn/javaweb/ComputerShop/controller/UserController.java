package vn.javaweb.ComputerShop.controller;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.javaweb.ComputerShop.component.MessageComponent;
import vn.javaweb.ComputerShop.domain.dto.request.*;
import vn.javaweb.ComputerShop.domain.dto.response.*;
import vn.javaweb.ComputerShop.handleException.BusinessException;
import vn.javaweb.ComputerShop.service.product.ProductService;
import vn.javaweb.ComputerShop.service.upload.UploadService;
import vn.javaweb.ComputerShop.service.user.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("${api.prefix.users}")
public class UserController {
    private final ProductService productService;
    private final UserService userService;
    private final UploadService uploadService;
    private final MessageComponent messageComponent;


    @GetMapping(value = "/profile")
    public ResponseEntity<ApiResponseT<UserProfileUpdateDTO>> getMe() {
        UserProfileUpdateDTO userProfileUpdateDTO = this.userService.handleGetDataUserToProfile();
        return ResponseEntity.ok().body(ApiResponseT.<UserProfileUpdateDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Get profile user successfully")
                .data(userProfileUpdateDTO)
                .build());
    }

    @PostMapping(value = "/profile")
    public ResponseEntity<ApiResponseT<?>> postUpdateProfile(@Valid @RequestBody UserProfileUpdateDTO userProfileUpdateDTO,
                                                             Locale locale) {
        this.userService.handleUpdateProfile(userProfileUpdateDTO , locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.profile.update.success", locale))
                .data(userProfileUpdateDTO)
                .build());
    }


    @PatchMapping(value = "/profile/change-password")
    public ResponseEntity<ApiResponseT<?>> postUpdateChangePass(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                                                Locale locale) {
        if (!changePasswordDTO.getNewPassword().trim().equals(changePasswordDTO.getConfirmNewPassword().trim())) {
            throw new BusinessException(messageComponent.getLocalizedMessage("user.password.update.error.newPasswordMismatch", locale));
        }
        this.userService.handleUpdatePassword(changePasswordDTO, locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.password.update.success", locale))
                .build());
    }

    @PatchMapping(value = "/profile/avatar")
    public ResponseEntity<ApiResponseT<?>> postUpdateAvatar(Locale locale,
                                                            @RequestParam("avatarFile") MultipartFile avatarFile) {
        this.userService.handleUpdateAvatar(avatarFile, locale);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message(messageComponent.getLocalizedMessage("user.avatar.update.success", locale))
                .build());
    }

    @PostMapping("face-data")
    public ResponseEntity<ApiResponseT<?>> postUpdateFaceData (@RequestBody FaceDataRequest faceDataRequest) {
        this.userService.handleUpdateEmbeddingFaceData(faceDataRequest);
        return ResponseEntity.ok().body(ApiResponseT.builder()
                .status(HttpStatus.OK.value())
                .message("Update face data successfully")
                .build());
    }
}
