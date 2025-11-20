package vn.javaweb.ComputerShop.domain.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDetailsListDTO {

    private List<CartDetailOneRqDTO> cartDetailOne;
}
