package com.onlineShopping.exception;

public class DuplicatePreferenceException extends RuntimeException {
    public DuplicatePreferenceException() {
    }

    public DuplicatePreferenceException(String message) {
        super(message);
    }
}
