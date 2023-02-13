package com.onlineShopping.controller;

import com.onlineShopping.dto.CartDTO;
import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.dto.PreferenceDTO;
import com.onlineShopping.exception.ItemNotAvailableException;
import com.onlineShopping.model.*;
import com.onlineShopping.service.interfaceService.CartService;
import com.onlineShopping.service.interfaceService.CataloguePreferenceService;
import com.onlineShopping.service.interfaceService.OrderService;
import com.onlineShopping.service.interfaceService.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CataloguePreferenceService cataloguePreferenceService;
    @Autowired
    private CartService cartService;

    //endpoint for registering the user
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody User user) {
        log.info("service=UserController; method=registerUser(); message=userController is accessed");
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        User result = userService.registration(user);
        if (result != null) {
            responseMap.put("message", "User " + user.getFirstName() + ", registered Successfully!");
            response = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
            log.info("user " + user.getFirstName() + " registered");
        } else {
            responseMap.put("message", "User not created");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //endpoint for validating user credentials
    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestBody User user) {
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        boolean result = userService.validateUserLogin(user.getEmail(), user.getPassword());
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        if (result) {
            log.info("service=UserController; method=userLogin(); message=SUCCESSFUL LOGIN");
            responseMap.put("message", "Login Successful!");
            response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            log.info("service=UserController; method=userLogin(); message=INVALID CREDENTIALS");
            responseMap.put("message", "Invalid email or password, please check and try again!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //endpoint for placing an order
    @PostMapping("/placeOrder")
    public ResponseEntity<Object> placeOrder(@RequestBody OrderDTO orderDTO) throws ItemNotAvailableException, MessagingException {
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        ResponseEntity<Object> response;
        Order order = orderService.placeOrder(orderDTO);
        if (order != null) {
            response = new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Order did not get placed.");
            response = new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //endpoint for adding a preference
    @PostMapping("/addPreference")
    public ResponseEntity<Object> addPreference(@RequestBody PreferenceDTO preferenceDTO) {
        ResponseEntity<Object> response;
        CataloguePreference preference = cataloguePreferenceService.addPreference(preferenceDTO);
        if (preference != null) {
            response = new ResponseEntity<>(preference, HttpStatus.OK);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Preference did not get saved.");
            response = new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //endpoint for removing a preference
    @PostMapping("/removePreference")
    public ResponseEntity<Object> removePreference(@RequestBody PreferenceDTO preferenceDTO) {
        ResponseEntity<Object> response;
        boolean removePreference = cataloguePreferenceService.removePreference(preferenceDTO);
        Map<String, String> responseMap = new HashMap<>();
        if (removePreference) {
            responseMap.put("message", "Preference got removed");

        } else {
            responseMap.put("message", "Preference could not get removed");
        }
        response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return response;
    }

    //endpoint for adding an item in cart
    @PostMapping("/addToCart")
    public ResponseEntity<Object> addToCart(@RequestBody CartDTO cartDTO) {
        ResponseEntity<Object> response;
        Cart cart = cartService.addItemToCart(cartDTO);
        if (cart != null) {
            response = new ResponseEntity<>(cart, HttpStatus.OK);
        } else {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Cart did not get created!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    //endpoint for removing an item from cart
    @PostMapping("/removeFromCart")
    public ResponseEntity<Object> removeFromCart(@RequestBody CartDTO cartDTO) {
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        boolean result = cartService.removeItemFromCart(cartDTO);
        if (result) {
            responseMap.put("message", "Item removed successfully");
            response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            responseMap.put("message", "Item did not get removed");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping("/getCart/{email}")
    public ResponseEntity<Object> getCartByEmail(@PathVariable String email){
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        Cart cart = cartService.getCartByEmail(email);
        if(cart!=null){
            response = new ResponseEntity<>(cart, HttpStatus.OK);
        } else {
            log.error("service=UserController; method=getCartByEmail(); message=CART NOT FOUND");
            responseMap.put("message", "Cart not found!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping("/getAllItems")
    public ResponseEntity<Object> getAllItems(@RequestParam int pageNo, @RequestParam int pageSize){
        ResponseEntity<Object> response;
        List<Item> itemList;
        itemList = userService.getAllItems(pageNo, pageSize);
        Map<String, String> responseMap = new HashMap<>();
        if (itemList != null) {
            log.info("service=UserController; method=getAllItems(); message={} record(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        } else {
            log.error("service=UserController; method=getAllItems(); message=ITEMS NOT RETRIEVED");
            responseMap.put("message", "Item not found!");
            response = new ResponseEntity<>(responseMap, HttpStatusCode.valueOf(404));
        }
        return response;
    }

    @GetMapping("/getItems/{category}")
    public ResponseEntity<Object> getItemsByCategory(@PathVariable("category") String category) {
        ResponseEntity<Object> response;
        List<Item> itemList;
        itemList = userService.getItemsByCategory(category);
        Map<String, String> responseMap = new HashMap<>();
        if (itemList != null) {
            log.info("service=UserController; method=getItemsByCategory(); message={} item(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        } else {
            log.error("service=UserController; method=getItemsByCategory(); message=ITEM NOT FOUND");
            responseMap.put("message", "Item not found!");
            response = new ResponseEntity<>(responseMap, HttpStatusCode.valueOf(404));
        }
        return response;
    }
}
