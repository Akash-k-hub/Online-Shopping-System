package com.onlineShopping.advice;

import com.onlineShopping.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArguments(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        log.error("service=ApplicationExceptionHandler; method=handleInvalidArguments; message=INVALID ARGUMENT(S) PASSED");
        log.error("{}", errorMap);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("email", "Email already exists!");
        log.error("service=ApplicationExceptionHandler; method=handleDuplicateKeyException; message=DUPLICATE EMAIL");
        log.error("{}", errorMap);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleUserNotFoundException; message=USER NOT FOUND!");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public final ResponseEntity<Object> handleAdminNotFoundException(AdminNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleAdminNotFoundException; message=ADMIN NOT FOUND!");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public final ResponseEntity<Object> handleCartNotFoundException(CartNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleCartNotFoundException; message=CART NOT FOUND!");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    public final ResponseEntity<Object> handleItemNotAvailableException(ItemNotAvailableException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleItemNotAvailableException; message=ITEM OUT OF STOCK!");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleItemNotFoundException; message=ITEM NOT FOUND");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(ItemQuantityNotAvailableException.class)
    public final ResponseEntity<Object> handleItemQuantityNotAvailableException(ItemQuantityNotAvailableException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleItemQuantityNotAvailableException; message=ITEM(S) QUANTITY IS NOT AVAILABLE");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPreferenceToRemoveException.class)
    public final ResponseEntity<Object> handleNoPreferenceToRemoveException(NoPreferenceToRemoveException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleNoPreferenceToRemoveException; message=PREFERENCE LIST IS EMPTY");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatePreferenceException.class)
    public final ResponseEntity<Object> handleDuplicatePreferenceException(DuplicatePreferenceException exception) {
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.error("service=ApplicationExceptionHandler; method=handleDuplicatePreferenceException; message=PREFERENCE ALREADY PRESENT");
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}