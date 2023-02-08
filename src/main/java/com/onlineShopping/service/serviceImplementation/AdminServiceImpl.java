package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.repository.AdminRepository;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.service.interfaceService.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        try {
            return adminRepository.save(admin);
        } catch (Exception exception) {
            throw new UnableToSaveException("Admin not saved!");
        }
    }

    @Override
    public Item saveItem(Item item) {
        try {
            log.info("service=AdminServiceImpl; method=saveItem(); message=adding item");
            itemRepository.save(item);
            return item;
        } catch (Exception exception) {
            throw new UnableToSaveException("Item not saved!");
        }
    }

    @Override
    public List<Item> getAllItems() {
        log.info("service=AdminServiceImpl; method=getAllItems(); message=fetching items from db");
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        List<Item> itemList = new ArrayList<>();
        List<Item> savedItems = itemRepository.itemsByCategory(category);
        for (Item item : savedItems) {
            Item displayItem = new Item();
            displayItem.setItemId(item.getItemId());
            displayItem.setItemName(item.getItemName());
            displayItem.setPrice(item.getPrice());
            displayItem.setQuantity(item.getQuantity());

            itemList.add(displayItem);
        }
        log.info("service=AdminServiceImpl; method=getItemsByCategory(); message=fetching items by category");
        return itemList;
    }
}
