package com.onlineShopping.exception;

public class UnableToSaveException extends RuntimeException {
    public UnableToSaveException() {
    }

    public UnableToSaveException(String message) {
        super(message);
    }
}
