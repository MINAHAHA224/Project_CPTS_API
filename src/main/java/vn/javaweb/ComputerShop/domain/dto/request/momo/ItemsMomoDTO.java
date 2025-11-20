package vn.javaweb.ComputerShop.domain.dto.request.momo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemsMomoDTO {
    private String id;             // SKU number
    private String name;           // Tên sản phẩm
    private String description;    // Miêu tả sản phẩm
    private String category;       // Phân loại ngành hàng
    private String imageUrl;       // Link hình ảnh
    private String manufacturer;   // Nhà sản xuất
    private Long price;            // Đơn giá (VND)
    private String currency;       // VND
    private Integer quantity;      // Số lượng (> 0)
    private String unit;           // Đơn vị đo lường
    private Long totalPrice;       // Tổng giá = price * quantity
    private Long taxAmount;        // Thuế
}
