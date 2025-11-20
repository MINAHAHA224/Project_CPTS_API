package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AuthMethodEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class AuthMethodEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.AuthMethodEntity#login_type
	 **/
	public static volatile SingularAttribute<AuthMethodEntity, String> login_type;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.AuthMethodEntity#external_id
	 **/
	public static volatile SingularAttribute<AuthMethodEntity, String> external_id;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.AuthMethodEntity
	 **/
	public static volatile EntityType<AuthMethodEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.AuthMethodEntity#user
	 **/
	public static volatile SingularAttribute<AuthMethodEntity, UserEntity> user;

	public static final String LOGIN_TYPE = "login_type";
	public static final String EXTERNAL_ID = "external_id";
	public static final String USER = "user";

}

