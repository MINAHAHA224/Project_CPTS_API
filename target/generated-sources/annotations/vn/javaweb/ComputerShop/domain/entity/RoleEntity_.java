package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(RoleEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class RoleEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.RoleEntity#name
	 **/
	public static volatile SingularAttribute<RoleEntity, String> name;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.RoleEntity#description
	 **/
	public static volatile SingularAttribute<RoleEntity, String> description;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.RoleEntity
	 **/
	public static volatile EntityType<RoleEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.RoleEntity#users
	 **/
	public static volatile ListAttribute<RoleEntity, UserEntity> users;

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String USERS = "users";

}

