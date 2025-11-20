package vn.javaweb.ComputerShop.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class LoginDTO {
    @NotBlank(message = "{LoginDTO.email.NotBlank}")
    @Email(message = "{LoginDTO.email.Email}")
    @Pattern(
            regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$",
            message = "{LoginDTO.email.Pattern}"
    )
    private String email;

    @NotBlank(message = "{LoginDTO.password.NotBlank}")
    @Size(min = 6, max = 20, message = "{LoginDTO.password.Size}")
    private String password;
}
