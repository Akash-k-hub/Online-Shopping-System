package com.onlineShopping.service.interfaceService;

import com.onlineShopping.model.Item;
import com.onlineShopping.model.User;

import java.util.List;

public interface UserService {

    User registration(User user);

    boolean validateUserLogin(String email, String password);

    List<Item> getAllItems(int pageNo, int pageSize);

    List<Item> getItemsByCategory(String category);
}
