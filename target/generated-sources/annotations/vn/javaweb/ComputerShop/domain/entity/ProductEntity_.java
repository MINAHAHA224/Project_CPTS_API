package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ProductEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class ProductEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#image
	 **/
	public static volatile SingularAttribute<ProductEntity, String> image;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#detailDesc
	 **/
	public static volatile SingularAttribute<ProductEntity, String> detailDesc;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#factory
	 **/
	public static volatile SingularAttribute<ProductEntity, String> factory;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#sold
	 **/
	public static volatile SingularAttribute<ProductEntity, Long> sold;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#orderDetails
	 **/
	public static volatile ListAttribute<ProductEntity, OrderDetailEntity> orderDetails;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#quantity
	 **/
	public static volatile SingularAttribute<ProductEntity, Long> quantity;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#price
	 **/
	public static volatile SingularAttribute<ProductEntity, Double> price;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#name
	 **/
	public static volatile SingularAttribute<ProductEntity, String> name;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#shortDesc
	 **/
	public static volatile SingularAttribute<ProductEntity, String> shortDesc;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity
	 **/
	public static volatile EntityType<ProductEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.ProductEntity#target
	 **/
	public static volatile SingularAttribute<ProductEntity, String> target;

	public static final String IMAGE = "image";
	public static final String DETAIL_DESC = "detailDesc";
	public static final String FACTORY = "factory";
	public static final String SOLD = "sold";
	public static final String ORDER_DETAILS = "orderDetails";
	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";
	public static final String NAME = "name";
	public static final String SHORT_DESC = "shortDesc";
	public static final String TARGET = "target";

}

