package vn.javaweb.ComputerShop.domain.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChangePasswordDTO {
    @NotBlank(message = "{ChangePasswordDTO.currentPassword.NotBlank}")
    private String currentPassword;

    @NotBlank(message = "{ChangePasswordDTO.newPassword.NotBlank}")
    @Size(min = 6, message = "{ChangePasswordDTO.newPassword.Size}")
    private String newPassword;

    @NotBlank(message = "{ChangePasswordDTO.confirmNewPassword.NotBlank}")
    private String confirmNewPassword;


}
