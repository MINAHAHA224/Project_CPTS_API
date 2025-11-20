package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ValidationError {
    private String field;
    private String message;
}
