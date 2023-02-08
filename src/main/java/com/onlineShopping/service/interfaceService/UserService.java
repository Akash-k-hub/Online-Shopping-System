package com.onlineShopping.service.interfaceService;

import com.onlineShopping.model.User;

public interface UserService {

    User registration(User user);

    boolean validateUserLogin(String email, String password);
}
