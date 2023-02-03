package com.onlineShopping.controller;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.ItemNotAvailable;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;
import com.onlineShopping.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@Valid @RequestBody  User user){
        log.info("service=UserController; method=registerUser(); message=userController is accessed");
        ResponseEntity<String> response = null;
        User result = userService.registration(user);

        if (result != null){
            response = new ResponseEntity<>("User " + user.getFirstName() + ", registered Successfully!", HttpStatus.CREATED);
            log.info("user "+ user.getFirstName() + " registered");
        }
        return response;
    }

    @PostMapping("/loginUser")
    public ResponseEntity<String> userLogin(@RequestBody User user){
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        boolean result = userService.validateUserLogin(user.getEmail(), user.getPassword());
        ResponseEntity<String> response = null;
        if(result){
            log.info("service=UserController; method=userLogin(); message=SUCCESSFUL LOGIN");
            response = new ResponseEntity<>("Login Successful!", HttpStatus.OK);
        }else {
            log.info("service=UserController; method=userLogin(); message=INVALID CREDENTIALS");
            response = new ResponseEntity<>("Invalid email or password, please check and try again!", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDTO orderDto) throws ItemNotAvailable {
        log.info("service=UserController; method=userLogin(); message=userController is accessed");
        ResponseEntity<Order> response = null;
        Order order = userService.placeOrder(orderDto);
        if(order!=null){
            response = new ResponseEntity<>(order, HttpStatus.OK);
        }
        return response;
    }
}
