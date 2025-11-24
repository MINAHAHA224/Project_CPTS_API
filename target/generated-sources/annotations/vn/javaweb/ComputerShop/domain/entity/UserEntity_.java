package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class UserEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#address
	 **/
	public static volatile SingularAttribute<UserEntity, String> address;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#role
	 **/
	public static volatile SingularAttribute<UserEntity, RoleEntity> role;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#fullName
	 **/
	public static volatile SingularAttribute<UserEntity, String> fullName;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#avatar
	 **/
	public static volatile SingularAttribute<UserEntity, String> avatar;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#cart
	 **/
	public static volatile ListAttribute<UserEntity, CartEntity> cart;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#userOtpEntities
	 **/
	public static volatile ListAttribute<UserEntity, UserOtpEntity> userOtpEntities;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#password
	 **/
	public static volatile SingularAttribute<UserEntity, String> password;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#faceData
	 **/
	public static volatile SingularAttribute<UserEntity, String> faceData;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#authMethodEntity
	 **/
	public static volatile SingularAttribute<UserEntity, AuthMethodEntity> authMethodEntity;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#phone
	 **/
	public static volatile SingularAttribute<UserEntity, String> phone;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#orders
	 **/
	public static volatile ListAttribute<UserEntity, OrderEntity> orders;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity
	 **/
	public static volatile EntityType<UserEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserEntity#email
	 **/
	public static volatile SingularAttribute<UserEntity, String> email;

	public static final String ADDRESS = "address";
	public static final String ROLE = "role";
	public static final String FULL_NAME = "fullName";
	public static final String AVATAR = "avatar";
	public static final String CART = "cart";
	public static final String USER_OTP_ENTITIES = "userOtpEntities";
	public static final String PASSWORD = "password";
	public static final String FACE_DATA = "faceData";
	public static final String AUTH_METHOD_ENTITY = "authMethodEntity";
	public static final String PHONE = "phone";
	public static final String ORDERS = "orders";
	public static final String EMAIL = "email";

}

