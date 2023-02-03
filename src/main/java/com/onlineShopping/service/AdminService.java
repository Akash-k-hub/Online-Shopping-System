package com.onlineShopping.service;

import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;

import java.util.List;

public interface AdminService {

    public Admin saveAdmin(Admin admin);

    public Item saveItem(Item item);

    public List<Item> getAllItems();

    public  List<Item> getItemsByCategory(String category);
}
