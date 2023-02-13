package com.onlineShopping.service;

import com.onlineShopping.model.User;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    void testRegistration() {
        User user = new User(
                "testId",
                "testFirstName",
                "testLastName",
                "male",
                "9874563215",
                "test@gmail.com",
                "testPassword",
                "testAddress"
        );

        when(userService.registration(user)).thenReturn(user);
        assertEquals("testFirstName", user.getFirstName());
        assertEquals("test@gmail.com", user.getEmail());
    }

    @Test
    void testValidateUserLogin() {
        User user = new User(
                "testId",
                "testFirstName",
                "testLastName",
                "male",
                "9874563215",
                "test@gmail.com",
                "testPassword",
                "testAddress"
        );
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoded = passwordEncoder.encode(user.getPassword());
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        boolean result = userService.validateUserLogin("test@gmail.com", "testPassword");
        assertTrue(!result);
    }
}
