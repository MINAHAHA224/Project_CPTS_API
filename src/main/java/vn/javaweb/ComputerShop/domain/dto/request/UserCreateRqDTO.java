package vn.javaweb.ComputerShop.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class UserCreateRqDTO {
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "{UserCreateRqDTO.email.Pattern}"
    )
    @NotBlank(message = "{UserCreateRqDTO.email.NotBlank}")
    private String email;

    @NotBlank(message = "{UserCreateRqDTO.password.NotBlank}")
    @Size(min = 6, max = 20, message = "{UserCreateRqDTO.password.Size}")
    private String password;

    @NotBlank(message = "{UserCreateRqDTO.phone.NotBlank}")
    @Pattern(regexp = "^\\d{10,11}$", message = "{UserCreateRqDTO.phone.Pattern}")
    private String phone;

    @NotBlank(message = "{UserCreateRqDTO.fullName.NotBlank}")
    @Size(max = 100, message = "{UserCreateRqDTO.fullName.Size}")
    private String fullName;

    @NotBlank(message = "{UserCreateRqDTO.address.NotBlank}")
    @Size(max = 255, message = "{UserCreateRqDTO.address.Size}")
    private String address;

    @NotBlank(message = "{UserCreateRqDTO.roleName.NotBlank}")
    private String roleName;

}
