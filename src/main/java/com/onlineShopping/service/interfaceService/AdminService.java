package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.ItemDTO;
import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;

import java.util.List;

public interface AdminService {

    Admin saveAdmin(Admin admin);

    Item saveItem(Item item);

    List<Item> getAllItems();

    List<Item> getItemsByCategory(String category);
}
