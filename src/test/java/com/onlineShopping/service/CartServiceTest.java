package com.onlineShopping.service;

import com.onlineShopping.constants.enums.Category;
import com.onlineShopping.dto.CartDTO;
import com.onlineShopping.exception.CartNotFoundException;
import com.onlineShopping.exception.ItemNotFoundException;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.Cart;
import com.onlineShopping.model.Item;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.CartRepository;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private ItemRepository itemRepository;

    @Test
    void testAddItemToCartUserNotFound() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        try {
            Cart cart = cartService.addItemToCart(new CartDTO("test1@gmail.com", item));
        } catch (UserNotFoundException exception) {
            assertEquals("User not registered, please register and try again!", exception.getMessage());
        }
    }

    @Test
    void testAddItemToCartItemNotFound() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        try {
            Cart cart = cartService.addItemToCart(new CartDTO("test@gmail.com", item));
        } catch (ItemNotFoundException exception) {
            assertEquals("Item not found!", exception.getMessage());
        }
    }

    @Test
    void testAddItemToCart() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        when(itemRepository.findByItemId(0000)).thenReturn(item);
        Cart cart = cartService.addItemToCart(new CartDTO("test@gmail.com", item));
        assertNotNull(cart);
        assertEquals(1, cart.getItemList().size());
    }

    @Test
    void testRemoveItemFromCart() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        when(itemRepository.findByItemId(0000)).thenReturn(item);
        Cart cart = cartService.addItemToCart(new CartDTO("test@gmail.com", item));
        when(cartRepository.findByEmail("test@gmail.com")).thenReturn(cart);
        boolean result = cartService.removeItemFromCart(new CartDTO("test@gmail.com", item));
        assertTrue(result);
    }

    @Test
    void testRemoveItemFromCartCartNotFound() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        when(itemRepository.findByItemId(0000)).thenReturn(item);
        Cart cart = cartService.addItemToCart(new CartDTO("test@gmail.com", item));
        try {
            boolean result = cartService.removeItemFromCart(new CartDTO("test@gmail.com", item));
            assertTrue(result);
        } catch (CartNotFoundException exception) {
            assertEquals("Cart not found!", exception.getMessage());
        }
    }

    @Test
    void testRemoveItemFromUserNotFound() {
        User user = new User("testID", "testFirstName", "testLastName", "male", "9876543210", "test@gmail.com", "abd@1234", "testAddress");
        Item item = new Item("testID", 0000, "testItemName", 9999, 15, Category.HARDWARE);
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
        when(itemRepository.findByItemId(0000)).thenReturn(item);
        Cart cart = cartService.addItemToCart(new CartDTO("test@gmail.com", item));
        try {
            boolean result = cartService.removeItemFromCart(new CartDTO("test1@gmail.com", item));
            assertTrue(result);
        } catch (UserNotFoundException exception) {
            assertEquals("User not registered, please register and try again!", exception.getMessage());
        }
    }

    @Test
    void testGetCartByEmail() {
        Map<Integer, Integer> itemList = new HashMap<>();
        itemList.put(0000, 5);
        itemList.put(1111, 5);
        Cart cart = new Cart("testID", "test@gmail.com", itemList);
        when(cartRepository.findByEmail("test@gmail.com")).thenReturn(cart);
        Cart getCart = cartService.getCartByEmail("test@gmail.com");
        assertNotNull(getCart);
    }

    @Test
    void testGetCartByEmailCartNotFound() {
        Map<Integer, Integer> itemList = new HashMap<>();
        itemList.put(0000, 5);
        itemList.put(1111, 5);
        Cart cart = new Cart("testID", "test@gmail.com", itemList);
        when(cartRepository.findByEmail("test@gmail.com")).thenReturn(cart);
        try {
            Cart getCart = cartService.getCartByEmail("test1@gmail.com");
            assertNull(getCart);
        } catch (CartNotFoundException exception) {
            assertEquals("Cart not found", exception.getMessage());
        }
    }
}
