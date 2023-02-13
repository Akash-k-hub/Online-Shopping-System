package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.AdminItemDTO;
import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;

import java.util.List;

public interface AdminService {

    Admin saveAdmin(Admin admin);

    Item saveItem(AdminItemDTO adminItemDTO);
}
