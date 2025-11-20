package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MomoRqDTO {
    private String partnerCode;         // Thông tin tích hợp
    private String subPartnerCode;      // Định danh tài khoản M4B
    private String storeName;           // Tên đối tác
    private String storeId;             // Mã cửa hàng
    private String requestId;           // ID duy nhất mỗi request
    private Long amount;                // Số tiền (1,000 - 50,000,000 VND)
    private String orderId;             // Mã đơn hàng
    private String orderInfo;           // Thông tin đơn hàng
    private Long orderGroupId;          // Nhóm đơn hàng do MoMo cung cấp
    private String redirectUrl;         // URL để chuyển hướng sau thanh toán
    private String ipnUrl;              // URL nhận kết quả IPN
    private String requestType;         // Loại request (e.g. captureWallet)
    private String extraData;           // Dữ liệu thêm (JSON base64)
    private List<ItemsMomoDTO> items;        // Danh sách sản phẩm
    private DeliveryInfoDTO deliveryInfo; // Thông tin giao hàng
    private UserInfoDTO userInfo;       // Thông tin người dùng
    private String referenceId;         // Mã tham chiếu phụ
    private Boolean autoCapture = true; // Tự động capture (mặc định true)
    private String lang;                // Ngôn ngữ (vi hoặc en)
    private String signature;
}
