package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(OrderEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class OrderEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#receiverAddress
	 **/
	public static volatile SingularAttribute<OrderEntity, String> receiverAddress;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#orderDetails
	 **/
	public static volatile ListAttribute<OrderEntity, OrderDetailEntity> orderDetails;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#receiverPhone
	 **/
	public static volatile SingularAttribute<OrderEntity, String> receiverPhone;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#totalPrice
	 **/
	public static volatile SingularAttribute<OrderEntity, Double> totalPrice;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#receiverName
	 **/
	public static volatile SingularAttribute<OrderEntity, String> receiverName;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#statusPayment
	 **/
	public static volatile SingularAttribute<OrderEntity, String> statusPayment;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#time
	 **/
	public static volatile SingularAttribute<OrderEntity, Date> time;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity
	 **/
	public static volatile EntityType<OrderEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#user
	 **/
	public static volatile SingularAttribute<OrderEntity, UserEntity> user;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#status
	 **/
	public static volatile SingularAttribute<OrderEntity, String> status;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderEntity#typePayment
	 **/
	public static volatile SingularAttribute<OrderEntity, String> typePayment;

	public static final String RECEIVER_ADDRESS = "receiverAddress";
	public static final String ORDER_DETAILS = "orderDetails";
	public static final String RECEIVER_PHONE = "receiverPhone";
	public static final String TOTAL_PRICE = "totalPrice";
	public static final String RECEIVER_NAME = "receiverName";
	public static final String STATUS_PAYMENT = "statusPayment";
	public static final String TIME = "time";
	public static final String USER = "user";
	public static final String STATUS = "status";
	public static final String TYPE_PAYMENT = "typePayment";

}

