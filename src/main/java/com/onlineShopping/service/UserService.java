package com.onlineShopping.service;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.ItemNotAvailable;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;

public interface UserService {

    User registration(User user);

    public boolean validateUserLogin(String email, String password);

    public Order placeOrder(OrderDTO orderDto) throws NullPointerException, ItemNotAvailable;


}
