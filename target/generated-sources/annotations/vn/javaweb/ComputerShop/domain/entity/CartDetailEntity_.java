package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(CartDetailEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class CartDetailEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.CartDetailEntity#product
	 **/
	public static volatile SingularAttribute<CartDetailEntity, ProductEntity> product;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.CartDetailEntity#quantity
	 **/
	public static volatile SingularAttribute<CartDetailEntity, Long> quantity;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.CartDetailEntity#price
	 **/
	public static volatile SingularAttribute<CartDetailEntity, Double> price;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.CartDetailEntity
	 **/
	public static volatile EntityType<CartDetailEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.CartDetailEntity#cart
	 **/
	public static volatile SingularAttribute<CartDetailEntity, CartEntity> cart;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";
	public static final String CART = "cart";

}

