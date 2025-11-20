package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductFilterAdRpDTO {
    private int page;

    private int totalPage;

    private List<ProductAdRpDTO> listProduct;
}
