package vn.javaweb.ComputerShop.handleException;


public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
