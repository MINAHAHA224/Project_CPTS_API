package vn.javaweb.ComputerShop.domain.dto.request;

import lombok.*;
import vn.javaweb.ComputerShop.service.validator.ResetPasswordChecked;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ResetPasswordChecked
public class ResetPasswordDTO {

    private String email;
    private String password;
    private String confirmPassword;
}
