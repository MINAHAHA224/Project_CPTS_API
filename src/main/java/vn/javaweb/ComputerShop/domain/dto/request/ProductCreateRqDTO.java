package vn.javaweb.ComputerShop.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Validated
public class ProductCreateRqDTO {
    @NotBlank(message = "{ProductCreateRqDTO.name.NotBlank}")
    @Size(max = 100, message = "{ProductCreateRqDTO.name.Size}")
    private String name;

    @Positive(message = "{ProductCreateRqDTO.price.Positive}")
    private double price;

    @NotBlank(message = "{ProductCreateRqDTO.detailDesc.NotBlank}")
    private String detailDesc;

    @NotBlank(message = "{ProductCreateRqDTO.shortDesc.NotBlank}")
    @Size(max = 255, message = "{ProductCreateRqDTO.shortDesc.Size}")
    private String shortDesc;

    @NotNull(message = "{ProductCreateRqDTO.quantity.NotNull}")
    @Min(value = 1, message = "{ProductCreateRqDTO.quantity.Min}")
    private Long quantity;

    @NotBlank(message = "{ProductCreateRqDTO.factory.NotBlank}")
    private String factory;

    @NotBlank(message = "{ProductCreateRqDTO.target.NotBlank}")
    private String target;
}
