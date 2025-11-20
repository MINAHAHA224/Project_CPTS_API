package vn.javaweb.ComputerShop.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {
    @NotBlank(message = "{ForgotPasswordDTO.email.NotBlank}")
    @Email(message = "{ForgotPasswordDTO.email.Email}")
    @Pattern(
            regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$",
            message = "{ForgotPasswordDTO.email.Pattern}"
    )
    private String email;
}
