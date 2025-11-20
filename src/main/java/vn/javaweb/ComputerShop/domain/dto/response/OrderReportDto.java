package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderReportDto {
    private Long id; // Order ID
    private Date orderTime;
    private String customerFullName; // Người đặt hàng
    private String customerEmail;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Double totalPrice;
    private String orderStatus;
    private String paymentType;
    private String paymentStatus;
    private String productDetails; // Chuỗi mô tả sản phẩm: "Tên SP1 (SL1), Tên SP2 (SL2)"
}
