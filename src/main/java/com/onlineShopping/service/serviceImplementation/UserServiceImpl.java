package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.Item;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    //code for registering the user
    @Transactional
    @Override
    public User registration(User user) {
        log.info("service=UserServiceImpl; method=registration(); message=saving to repository");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            return userRepository.save(user);
        } catch (Exception exception) {
            log.error("service=UserServiceImpl; method=registration(); message=USER NOT REGISTERED");
            throw new UnableToSaveException("User not registered!");
        }
    }

    //code for validating the user
    @Override
    public boolean validateUserLogin(String email, String password) {
        log.info("service=UserServiceImpl; method=validateUserLogin(); message=validating user");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Invalid email, please check and try again!");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info("service=UserServiceImpl; method=validateUserLogin(); message=validating hashed password");
        boolean encodedPassword = passwordEncoder.matches(password, user.getPassword());
        if (encodedPassword) {
            log.info("service=UserServiceImpl; method=validateUserLogin(); message=credentials authenticated");
            return true;
        }
        return false;
    }

    @Override
    public List<Item> getAllItems(int pageNo, int pageSize) {
        log.info("service=UserServiceImpl; method=getAllItems(); message=fetching items from db");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return itemRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Item> getItemsByCategory(String category) {
        List<Item> itemList = new ArrayList<>();
        List<Item> savedItems = itemRepository.itemsByCategory(category);
        for (Item item : savedItems) {
            //preparing items to display
            Item displayItem = new Item();
            displayItem.setItemId(item.getItemId());
            displayItem.setItemName(item.getItemName());
            displayItem.setPrice(item.getPrice());
            displayItem.setQuantity(item.getQuantity());
            itemList.add(displayItem);
        }
        log.info("service=UserServiceImpl; method=getItemsByCategory(); message=fetching items by category");
        return itemList;
    }
}
