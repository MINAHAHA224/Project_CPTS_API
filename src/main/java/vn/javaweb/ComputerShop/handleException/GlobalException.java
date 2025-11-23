package vn.javaweb.ComputerShop.handleException;



import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.javaweb.ComputerShop.domain.dto.response.ErrorResponse;
import vn.javaweb.ComputerShop.domain.dto.response.ValidationError;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException (MethodArgumentNotValidException e , WebRequest webRequest){
        ErrorResponse  body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setMessage("Validation failed");
        body.setError("Validation error");
        List<ValidationError> validationErrors = e.getFieldErrors().stream().map(
                er -> ValidationError.builder()
                        .field(er.getField())
                        .message(er.getDefaultMessage())
                        .build()
        ).toList();
        body.setErrorDetails(validationErrors);
        body.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        log.error(" [MethodArgumentNotValidException] at  = {} , {}" , body.getPath() , body);

        return new ResponseEntity<>(body ,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler( {Exception.class , RuntimeException.class , NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleGlobalException ( Exception e , WebRequest webRequest ){
        ErrorResponse  body = new ErrorResponse();
        body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setMessage("Something went wrong, please contact support.");
        body.setError("Internal server error");
        body.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        log.error(" [GlobalException] at = {} , {}" , body.getPath() , e.getMessage());
        return new ResponseEntity<>(body ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler( DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException ( DataIntegrityViolationException e , WebRequest webRequest ){
        ErrorResponse  body = new ErrorResponse();
        body.setStatus(HttpStatus.CONFLICT.value());
        body.setMessage("Something went wrong, please contact support.");
        body.setError("Data integrity violation error");
        body.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        log.error(" [DataIntegrityViolationException] at = {} , {}" , body.getPath() , e.getMessage());
        return new ResponseEntity<>(body ,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException (BusinessException e , WebRequest webRequest ){
        ErrorResponse  body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setMessage(e.getMessage());
        body.setError("Business error");
        body.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        log.error(" [BusinessException] at = {} , {}", body.getPath() , e.getMessage());
        return new ResponseEntity<>(body ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse>  handleAuthenticationException ( AuthException e , WebRequest webRequest){
        ErrorResponse  body = new ErrorResponse();
        body.setStatus(HttpStatus.BAD_REQUEST.value());
        body.setMessage(e.getMessage());
//        body.setMessage("Something went wrong, please contact support.");
        body.setError("Authentication error");
        body.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        log.error(" [AuthException] at = {} , {}" , body.getPath() , e.getMessage());
        return new ResponseEntity<>(body ,HttpStatus.UNAUTHORIZED);
    }
}
