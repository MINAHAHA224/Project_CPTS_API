package vn.javaweb.ComputerShop.domain.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InformationDTO implements Serializable {
    private Long id;
    private String email;
    private String role;
    private String fullName;
    private String avatar;
//    private int sum;
    private String tokenJWT;
}
