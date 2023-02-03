package com.onlineShopping.exception;

public class ItemNotAvailable extends Exception{

    public ItemNotAvailable() {
        super();
    }

    public ItemNotAvailable(String message) {
        super(message);
    }
}
