package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CountElementDTO {
    private long countElementUser ;
    private long countElementProduct ;
    private long countElementOrder ;

}
