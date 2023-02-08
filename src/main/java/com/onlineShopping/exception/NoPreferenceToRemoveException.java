package com.onlineShopping.exception;

public class NoPreferenceToRemoveException extends RuntimeException {
    public NoPreferenceToRemoveException() {
    }

    public NoPreferenceToRemoveException(String message) {
        super(message);
    }
}
