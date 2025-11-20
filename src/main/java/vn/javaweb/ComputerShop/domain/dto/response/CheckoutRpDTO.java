package vn.javaweb.ComputerShop.domain.dto.response;


import lombok.*;
import vn.javaweb.ComputerShop.domain.dto.request.InfoOrderRqDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CheckoutRpDTO {
    private List<CartDetailRpDTO> cartDetails;
    private double totalPrice;
    private InfoOrderRqDTO infoOrderRqDTO;
}
