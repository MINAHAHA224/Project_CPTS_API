package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DataloginDTO {
    private String accessToken ;
    private String refreshToken;
    private int expiresIn;
    private String tokenType;
}
