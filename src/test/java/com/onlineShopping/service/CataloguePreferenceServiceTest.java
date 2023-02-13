package com.onlineShopping.service;

import com.onlineShopping.dto.PreferenceDTO;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.CataloguePreference;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.CataloguePreferenceRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.CataloguePreferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CataloguePreferenceServiceTest {
    @Autowired
    private CataloguePreferenceService cataloguePreferenceService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CataloguePreferenceRepository cataloguePreferenceRepository;

    @Test
    void testAddPreference() {
        User user = new User("testID",
                "testFirstName",
                "testLastName",
                "male",
                "9876543210",
                "test@gmail.com",
                "abd@1234",
                "testAddress");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);

        CataloguePreference preference = cataloguePreferenceService.addPreference(new PreferenceDTO("test@gmail.com", "SHOES"));

        assertNotNull(preference);
        assertEquals("test@gmail.com", preference.getEmail());
        assertEquals("SHOES", preference.getPreferredCategory().get(0));
    }

    @Test
    void testRemovePreference() {
        User user = new User("testID",
                "testFirstName",
                "testLastName",
                "male",
                "9876543210",
                "test@gmail.com",
                "abd@1234",
                "testAddress");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);

        CataloguePreference preference = cataloguePreferenceService.addPreference(new PreferenceDTO("test@gmail.com", "SHOES"));
        when(cataloguePreferenceRepository.findByEmail("test@gmail.com")).thenReturn(preference);
        boolean result = cataloguePreferenceService.removePreference(new PreferenceDTO("test@gmail.com", "SHOES"));

        assertTrue(result);
    }

    @Test
    void testRemovePreferenceInvalidEmail() {
        User user = new User("testID",
                "testFirstName",
                "testLastName",
                "male",
                "9876543210",
                "test@gmail.com",
                "abd@1234",
                "testAddress");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);

        CataloguePreference preference = cataloguePreferenceService.addPreference(new PreferenceDTO("test@gmail.com", "SHOES"));
        when(cataloguePreferenceRepository.findByEmail("test@gmail.com")).thenReturn(preference);
        try {
            boolean result = cataloguePreferenceService.removePreference(new PreferenceDTO("test1@gmail.com", "SHOES"));
            assertFalse(result);
        } catch (UserNotFoundException exception) {
            assertEquals("User not found, please register and try again!", exception.getMessage());
        }
    }
}
