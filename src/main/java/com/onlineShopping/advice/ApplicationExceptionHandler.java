package com.onlineShopping.advice;

import com.onlineShopping.exception.ItemNotAvailable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalidArguments(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error->{
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        log.warn("service=ApplicationExceptionHandler; method=handleInvalidArguments; message=INVALID ARGUMENT(S) PASSED");
        log.error("{}",errorMap);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DuplicateKeyException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("email", "Email already exists!");
        log.warn("service=ApplicationExceptionHandler; method=handleDuplicateKeyException; message=DUPLICATE EMAIL");
        log.error("{}",errorMap);
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException exception){
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.warn("service=ApplicationExceptionHandler; method=handleNullPointerException; message=NULL POINTER EXCEPTION");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(ItemNotAvailable.class)
    public final ResponseEntity<Map<String, String>> handleItemNotAvailableException(ItemNotAvailable exception){
        Map<String, String> errorMap = new HashMap<>();
        String message = exception.getMessage();
        errorMap.put("message", message);
        log.warn("service=ApplicationExceptionHandler; method=handleItemNotAvailableException; message=ITEM OUT OF STOCK");
        return new ResponseEntity<>(errorMap, HttpStatusCode.valueOf(404));
    }

}
