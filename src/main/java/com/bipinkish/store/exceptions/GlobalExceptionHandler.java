package com.bipinkish.store.exceptions;

import com.bipinkish.store.dto.ErrorDto;
import com.bipinkish.store.payments.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleInvalidMessage() {
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid request body"));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Cart not found"));
    }

    @ExceptionHandler(CartItemsNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartItemsNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Cart items not found"));
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Product not found"));
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorDto> handleUserServiceException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({UserUnAuthorizedExcception.class, BadCredentialsException.class})
    public ResponseEntity<ErrorDto> handleUserUnAuthorizedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleForbiddenException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(ex.getMessage()));
    }
}
