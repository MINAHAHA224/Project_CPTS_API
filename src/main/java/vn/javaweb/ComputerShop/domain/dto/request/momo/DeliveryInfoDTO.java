package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryInfoDTO {
    private String deliveryAddress; // Địa chỉ giao hàng
    private String deliveryFee;     // Phí giao hàng (dạng chuỗi)
    private String quantity;
}
