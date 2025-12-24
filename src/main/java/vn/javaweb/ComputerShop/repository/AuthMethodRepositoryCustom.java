package vn.javaweb.ComputerShop.repository;

import vn.javaweb.ComputerShop.domain.entity.AuthMethodEntity;
import vn.javaweb.ComputerShop.domain.entity.UserEntity;

import java.time.LocalDateTime;

public interface AuthMethodRepositoryCustom {

    AuthMethodEntity findAuthMethodToVerifyOtp (UserEntity user , String OTP , LocalDateTime date);
}
