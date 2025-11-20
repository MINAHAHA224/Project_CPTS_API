package vn.javaweb.ComputerShop.domain.dto.request;

import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductFilterDTO {

    private String page;
    private List<String> factory;
    private List<String> target;
    private List<String> price;
    private String sort;
}
