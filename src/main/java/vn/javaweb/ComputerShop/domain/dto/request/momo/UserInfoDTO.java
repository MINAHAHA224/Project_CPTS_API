package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoDTO {
    private String name;         // Tên người dùng
    private String phoneNumber;  // Số điện thoại
    private String email;        // Địa chỉ email
}
