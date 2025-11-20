package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartRpDTO {
    private List<CartDetailRpDTO> cartDetails;
    private double totalPrice;




}
