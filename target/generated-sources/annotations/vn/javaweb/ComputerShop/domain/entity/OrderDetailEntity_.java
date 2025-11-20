package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(OrderDetailEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class OrderDetailEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity#product
	 **/
	public static volatile SingularAttribute<OrderDetailEntity, ProductEntity> product;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity#quantity
	 **/
	public static volatile SingularAttribute<OrderDetailEntity, Long> quantity;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity#price
	 **/
	public static volatile SingularAttribute<OrderDetailEntity, Double> price;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity
	 **/
	public static volatile EntityType<OrderDetailEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.OrderDetailEntity#order
	 **/
	public static volatile SingularAttribute<OrderDetailEntity, OrderEntity> order;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";
	public static final String ORDER = "order";

}

