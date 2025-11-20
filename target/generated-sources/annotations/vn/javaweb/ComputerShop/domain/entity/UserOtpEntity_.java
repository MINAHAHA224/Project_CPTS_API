package vn.javaweb.ComputerShop.domain.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

@StaticMetamodel(UserOtpEntity.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class UserOtpEntity_ extends vn.javaweb.ComputerShop.domain.entity.abstracts.BaseIdEntity_ {

	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity#createdAt
	 **/
	public static volatile SingularAttribute<UserOtpEntity, LocalDateTime> createdAt;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity#otpCode
	 **/
	public static volatile SingularAttribute<UserOtpEntity, String> otpCode;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity
	 **/
	public static volatile EntityType<UserOtpEntity> class_;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity#user
	 **/
	public static volatile SingularAttribute<UserOtpEntity, UserEntity> user;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity#expiredTime
	 **/
	public static volatile SingularAttribute<UserOtpEntity, LocalDateTime> expiredTime;
	
	/**
	 * @see vn.javaweb.ComputerShop.domain.entity.UserOtpEntity#isUsed
	 **/
	public static volatile SingularAttribute<UserOtpEntity, Boolean> isUsed;

	public static final String CREATED_AT = "createdAt";
	public static final String OTP_CODE = "otpCode";
	public static final String USER = "user";
	public static final String EXPIRED_TIME = "expiredTime";
	public static final String IS_USED = "isUsed";

}

