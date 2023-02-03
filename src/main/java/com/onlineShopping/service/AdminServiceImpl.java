package com.onlineShopping.service;

import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.repository.AdminRepository;
import com.onlineShopping.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Admin saveAdmin(Admin admin) {
        log.info("service=AdminServiceImpl; method=saveAdmin(); message=saving admin");
        return adminRepository.save(admin);
    }

    @Override
    public Item saveItem(Item item) {
        log.info("service=AdminServiceImpl; method=saveItem(); message=adding item");
        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAllItems() {
        log.info("service=AdminServiceImpl; method=getAllItems(); message=fetching items from db");
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        List<Item> newList = new ArrayList<>();
        List<Item> itemList = itemRepository.itemsByCategory(category);
        for(Item item : itemList){
            Item newItem = new Item();
            newItem.setItemId(item.getItemId());
            newItem.setItemName(item.getItemName());
            newItem.setPrice(item.getPrice());
            newItem.setQuantity(item.getQuantity());

            newList.add(newItem);
        }
        log.info("service=AdminServiceImpl; method=getItemsByCategory(); message=fetching items by category");
        return newList;
    }
}
