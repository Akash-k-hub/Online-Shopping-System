package com.onlineShopping.model;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {

    private List<Item> itemsInCart;

    private int sumTotal;
}
