package com.onlineShopping.controller;

import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.service.interfaceService.AdminService;
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

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<Object> saveAdmin(@Valid @RequestBody Admin admin) {
        log.info("service=AdminController; method=saveAdmin(); message=saving admin");
        ResponseEntity<Object> response;
        Map<String, String> responseMap = new HashMap<>();
        Admin result = adminService.saveAdmin(admin);
        if (result != null) {
            responseMap.put("message", "Admin " + result.getFirstName() + ", registered Successfully!");
            response = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        } else {
            responseMap.put("message", "Admin not created!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/saveItem")
    public ResponseEntity<Object> saveItem(@Valid @RequestBody Item item) {
        ResponseEntity<Object> response;
        Item result = adminService.saveItem(item);
        Map<String, String> responseMap = new HashMap<>();
        if (result != null) {
            log.info("service=AdminController; method=saveAdmin(); message=item - {}, got saved", item.getItemName());
            responseMap.put("message", "Item added successfully!");
            response = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        } else {
            log.warn("service=AdminController; method=saveAdmin(); message=item - {}, did not get saved", item.getItemName());
            responseMap.put("message", "Item did not get added!");
            response = new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping("/getAllItems")
    public ResponseEntity<Object> getAllItems() {
        log.info("service=AdminController; method=getAllItems(); message=retrieving all items");
        ResponseEntity<Object> response;
        List<Item> itemList;
        itemList = adminService.getAllItems();
        Map<String, String> responseMap = new HashMap<>();
        if (itemList != null) {
            log.info("service=AdminController; method=getAllItems(); message={} record(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        } else {
            responseMap.put("message", "Item not found!");
            response = new ResponseEntity<>(responseMap, HttpStatusCode.valueOf(404));
        }
        return response;
    }

    @GetMapping("/getItems/{category}")
    public ResponseEntity<Object> getItemsByCategory(@PathVariable("category") String category) {
        log.info("service=AdminController; method=getItemsByCategory(); message=retrieving items by category");
        ResponseEntity<Object> response;
        List<Item> itemList;
        itemList = adminService.getItemsByCategory(category);
        Map<String, String> responseMap = new HashMap<>();
        if (itemList != null) {
            log.info("service=AdminController; method=getItemsByCategory(); message={} item(s) retrieved", itemList.size());
            response = new ResponseEntity<>(itemList, HttpStatus.OK);
        } else {
            responseMap.put("message", "Item not found!");
            response = new ResponseEntity<>(responseMap, HttpStatusCode.valueOf(404));
        }
        return response;
    }
}
