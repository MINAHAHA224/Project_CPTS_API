package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserReportDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String roleName;
    private String authMethods; // Các phương thức đăng nhập, vd: "EMAIL, GOOGLE"
}
