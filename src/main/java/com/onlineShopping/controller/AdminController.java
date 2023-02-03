package com.onlineShopping.controller;

import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.service.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> saveAdmin(@Valid @RequestBody Admin admin){
        log.info("service=AdminController; method=saveAdmin(); message=saving user");
        ResponseEntity<String> response = null;
        Admin result = adminService.saveAdmin(admin);
        if(result!=null){
            response = new ResponseEntity<>("Admin "+ result.getFirstName() +", registered Successfully", HttpStatus.CREATED);
        }
        return response;
    }

    @PostMapping("/saveItem")
    public ResponseEntity<String> saveItem(@Valid @RequestBody Item item){
        log.info("service=AdminController; method=saveAdmin(); message=saving item - {}", item.getItemName());
        ResponseEntity<String> response = null;
        Item result = adminService.saveItem(item);
        if(item!=null){
            response = new ResponseEntity<>("Item added successfully!", HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/getAllItems")
    public ResponseEntity<List<Item>> getAllItems(){
        log.info("service=AdminController; method=getAllItems(); message=retrieving all items");
        ResponseEntity<List<Item>> response = null;
        List<Item> itemList = new ArrayList<>();
        itemList = adminService.getAllItems();
        if(itemList!=null){
            log.info("service=AdminController; method=getAllItems(); message={} record(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/getItems/{category}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable("category") String category){
        log.info("service=AdminController; method=getItemsByCategory(); message=retrieving items by category");
        ResponseEntity<List<Item>> response = null;
        List<Item> itemList = new ArrayList<>();
        itemList = adminService.getItemsByCategory(category);
        if (itemList!=null){
            log.info("service=AdminController; method=getItemsByCategory(); message={} item(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        }
        return response;
    }
}
