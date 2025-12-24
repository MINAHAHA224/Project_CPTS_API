package vn.javaweb.ComputerShop.domain.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity extends BaseIdEntity<Long> {


    private long quantity;
    private double price;

    // order_id : long
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    // product_id : long
    @ManyToOne
    @JoinColumn(name = "product_id" )
    private ProductEntity product;


}
