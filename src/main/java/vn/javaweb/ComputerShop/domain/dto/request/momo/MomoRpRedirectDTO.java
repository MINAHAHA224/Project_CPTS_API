package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MomoRpRedirectDTO {
    private String orderId;
    private String orderInfo;
    private String message;
}
