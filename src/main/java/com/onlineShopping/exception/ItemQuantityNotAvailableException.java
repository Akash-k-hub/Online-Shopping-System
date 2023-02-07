package com.onlineShopping.exception;

public class ItemQuantityNotAvailableException extends RuntimeException{
    public ItemQuantityNotAvailableException() {
    }

    public ItemQuantityNotAvailableException(String message) {
        super(message);
    }
}
