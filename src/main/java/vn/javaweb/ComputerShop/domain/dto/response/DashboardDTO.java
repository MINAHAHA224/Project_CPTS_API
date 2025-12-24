package vn.javaweb.ComputerShop.domain.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DashboardDTO {
    private long numberUser ;
    private long numberProduct ;
    private long numberOrder ;

}
