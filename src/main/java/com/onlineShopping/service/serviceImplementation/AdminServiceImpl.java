package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.dto.AdminItemDTO;
import com.onlineShopping.exception.AdminNotFoundException;
import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.repository.AdminRepository;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.service.interfaceService.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ItemRepository itemRepository;

    //code to register an admin
    @Transactional
    @Override
    public Admin saveAdmin(Admin admin) {
        log.info("service=AdminServiceImpl; method=saveAdmin(); message=saving admin");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        try {
            return adminRepository.save(admin);
        } catch (Exception exception) {
            log.error("ADMIN NOT SAVED");
            throw new UnableToSaveException("Admin not saved!");
        }
    }

    //code to save an item
    @Transactional
    @Override
    public Item saveItem(AdminItemDTO adminItemDTO) {
        if (adminRepository.findByEmail(adminItemDTO.getEmail()) == null) {
            log.error("service=AdminServiceImpl; method=saveItem(); message=ADMIN NOT FOUND");
            throw new AdminNotFoundException("Admin not found!");
        }
        Item item = adminItemDTO.getItem();
        try {
            log.info("service=AdminServiceImpl; method=saveItem(); message=adding item");
            itemRepository.save(item);
            return item;
        } catch (Exception exception) {
            log.error("service=AdminServiceImpl; method=saveItem(); message=ITEM NOT SAVED");
            throw new UnableToSaveException("Item not saved!");
        }
    }
}
