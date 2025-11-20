package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MomoRpDTO {
    private String partnerCode;        // Thông tin tích hợp
    private String requestId;          // ID của yêu cầu ban đầu
    private String orderId;            // Mã đơn hàng của đối tác
    private Long amount;               // Số tiền thanh toán
    private Long responseTime;         // Thời gian trả kết quả (timestamp)
    private String message;            // Mô tả lỗi (nếu có)
    private int resultCode;            // Mã kết quả
    private String payUrl;             // URL chuyển sang trang thanh toán MoMo
    private String deeplink;           // URL mở ứng dụng MoMo
    private String qrCodeUrl;          // Dữ liệu tạo QR code
    private String deeplinkMiniApp;    // URL mở mini app MoMo
    private String signature;          // Chữ ký xác nhận giao dịch
    private Long userFee;              // Phí người dùng chịu
}
