package com.onlineShopping.service;

import com.onlineShopping.constants.enums.Category;
import com.onlineShopping.dto.AdminItemDTO;
import com.onlineShopping.model.Admin;
import com.onlineShopping.model.Item;
import com.onlineShopping.repository.AdminRepository;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.service.interfaceService.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;
    @MockBean
    private AdminRepository adminRepository;
    @MockBean
    private ItemRepository itemRepository;

    @Test
    void testSaveAdmin() {
        Admin admin = new Admin("testID", "testFirstName", "testLastName", "test@gmail.com", "test@1234");

        when(adminService.saveAdmin(admin)).thenReturn(admin);
        assertNotNull(admin.getId());
        assertEquals("testFirstName", admin.getFirstName());
        assertEquals("test@gmail.com", admin.getEmail());
    }

    @Test
    void testSaveAdminForIllegalArgument() {
        Admin admin = new Admin("testID", null, null, "", "test@1234");
        Admin savedAdmin = adminService.saveAdmin(admin);
        assertNull(savedAdmin);
    }


    @Test
    void testSaveItem() {
        Admin admin = new Admin("testID", "testFirstName", "testLastName", "test@gmail.com", "test@1234");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        AdminItemDTO adminItemDTO = new AdminItemDTO("test@gmail.com", item);
        when(adminRepository.findByEmail(adminItemDTO.getEmail())).thenReturn(admin);
        Item savedItem = adminService.saveItem(adminItemDTO);
        assertNotNull(savedItem.getItemId());
        assertEquals("testID", savedItem.getId());
        assertEquals("testItemName", savedItem.getItemName());
        assertEquals(Category.HARDWARE, savedItem.getCategory());
    }

}
