package vn.javaweb.ComputerShop.domain.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInvoiceDTO {
    private long id;
    private Date time;
    private String typePayment;
    private String statusPayment;

    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    private String userEmail;

    List<OrderDetailRpDTO> orderDetails;

    private double totalPrice;
}
