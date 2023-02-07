package com.onlineShopping.controller;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.dto.PreferenceDTO;
import com.onlineShopping.exception.ItemNotAvailableException;
import com.onlineShopping.model.CataloguePreference;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;
import com.onlineShopping.service.interfaceService.CataloguePreferenceService;
import com.onlineShopping.service.interfaceService.OrderService;
import com.onlineShopping.service.interfaceService.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody  User user){
        log.info("service=UserController; method=registerUser(); message=userController is accessed");
        ResponseEntity<Object> response;
        Map<String,String> responseMap = new HashMap<>();
        User result = userService.registration(user);
        if (result != null){
            responseMap.put("message", "User " + user.getFirstName() + ", registered Successfully!");
            response = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
            log.info("user "+ user.getFirstName() + " registered");
        }else {
            responseMap.put("message", "User not created");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/loginUser")
    public ResponseEntity<Object> userLogin(@RequestBody User user){
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        boolean result = userService.validateUserLogin(user.getEmail(), user.getPassword());
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        if(result){
            log.info("service=UserController; method=userLogin(); message=SUCCESSFUL LOGIN");
            responseMap.put("message", "Login Successful!");
            response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        }else {
            log.info("service=UserController; method=userLogin(); message=INVALID CREDENTIALS");
            responseMap.put("message", "Invalid email or password, please check and try again!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Object> placeOrder(@RequestBody OrderDTO orderDTO) throws ItemNotAvailableException {
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        ResponseEntity<Object> response;
        Order order = orderService.placeOrder(orderDTO);
        if(order!=null){
            response = new ResponseEntity<>(order, HttpStatus.OK);
        }else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Order did not get placed.");
            response = new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/addPreference")
    public ResponseEntity<Object> addPreference(@RequestBody PreferenceDTO preferenceDTO){
        ResponseEntity<Object> response;
        CataloguePreference preference = cataloguePreferenceService.addPreference(preferenceDTO);
        if (preference!=null){
            response = new ResponseEntity<>(preference, HttpStatus.OK);
        }else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Preference did not get saved.");
            response = new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/removePreference")
    public ResponseEntity<Object> removePreference(@RequestBody PreferenceDTO preferenceDTO){
        ResponseEntity<Object> response;
        boolean removePreference = cataloguePreferenceService.removePreference(preferenceDTO);
        Map<String, String> responseMap = new HashMap<>();
        if (removePreference){
            responseMap.put("message", "Preference got removed");

        }else{
            responseMap.put("message", "Preference could not get removed");
        }
        response = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return response;
    }
}
