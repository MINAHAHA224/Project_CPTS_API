package vn.javaweb.ComputerShop.domain.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDetailOneRqDTO {

    private Long id;
    private Long quantity;
}
