package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.dto.CartDTO;
import com.onlineShopping.dto.ItemDTO;
import com.onlineShopping.exception.CartNotFoundException;
import com.onlineShopping.exception.ItemNotFoundException;
import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.Cart;
import com.onlineShopping.model.Item;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.CartRepository;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    //code for adding an item to the cart
    @Transactional
    @Override
    public Cart addItemToCart(CartDTO cartDTO) {
        log.info("service=CartServiceImpl; method=addToCart(); message=checking if user is registered");
        //check if user is registered or not
        User user = userRepository.findByEmail(cartDTO.getEmail());
        if (user != null) {
            Cart existingCart = cartRepository.findByEmail(cartDTO.getEmail());
            //check if user already has a cart
            log.info("service=CartServiceImpl; method=addToCart(); message=checking if user already has a cart");
            if (existingCart != null) {
                //cart exists, updating the cart
                Cart cart = updateCart(existingCart, cartDTO);
                return cart;
            } else {
                //cart not exist, creating a new cart
                Cart cart = createCart(cartDTO, user);
                return cart;
            }
        }
        log.error("service=CartServiceImpl; method=addToCart(); message=UN-REGISTERED USER");
        throw new UserNotFoundException("User not registered, please register and try again!");
    }

    //code for removing an item from cart
    @Transactional
    @Override
    public boolean removeItemFromCart(CartDTO cartDTO) {
        log.info("service=CartServiceImpl; method=removeItemFromCart(); message=checking if user is registered");
        //check if user is registered
        if (userRepository.findByEmail(cartDTO.getEmail()) != null) {
            Cart cart = cartRepository.findByEmail(cartDTO.getEmail());
            log.info("service=CartServiceImpl; method=removeItemFromCart(); message=checking if cart is empty");
            //check if there is a cart for user
            if (cart != null) {
                Map<Integer, Integer> itemsInCart = cart.getItemList();
                int itemKey = cartDTO.getItemInCart().getItemId();
                //check if item exists in the cart
                if (itemsInCart.containsKey(itemKey)) {
                    log.info("service=CartServiceImpl; method=removeItemFromCart(); message=removing item from the cart");
                    itemsInCart.remove(itemKey);
                } else {
                    log.error("service=CartServiceImpl; method=removeItemFromCart(); message=ITEM NOT PRESENT IN THE CART");
                    throw new ItemNotFoundException("Item not found!");
                }
                cart.setItemList(itemsInCart);
                try {
                    cartRepository.save(cart);
                    log.info("service=CartServiceImpl; method=removeItemFromCart(); message=saving the updated cart");
                    return true;
                } catch (Exception exception) {
                    log.error("service=CartServiceImpl; method=removeItemFromCart(); message=CART NOT SAVED");
                    throw new UnableToSaveException("Updated cart did not get saved!");
                }
            }
            log.error("service=CartServiceImpl; method=removeItemFromCart(); message=CART NOT FOUND");
            throw new CartNotFoundException("Cart not found!");
        }
        log.error("service=CartServiceImpl; method=removeItemFromCart(); message=UN-REGISTERED USER");
        throw new UserNotFoundException("User not registered, please register and try again!");
    }

    @Override
    public Cart getCartByEmail(String email) {
        Cart cart = cartRepository.findByEmail(email);
        if (cart!=null){
            return cart;
        }else {
            log.error("service=CartServiceImpl; method=getCartByEmail(); message=CART NOT FOUND");
            throw new CartNotFoundException("Cart not found");
        }
    }

    //code to update the existing cart of the user
    private Cart updateCart(Cart existingCart, CartDTO cartDTO) {
        log.info("service=CartServiceImpl; method=createCart(); message=cart exists");
        //check if cart already have that item -> update the quantity
        Map<Integer, Integer> existingItemList = existingCart.getItemList();
        int itemKey = cartDTO.getItemInCart().getItemId();
        int itemValue = cartDTO.getItemInCart().getQuantity();
        //adding/updating item in the cart
        log.info("service=CartServiceImpl; method=createCart(); message=updating the cart");
        existingItemList.put(itemKey, itemValue);
        existingCart.setItemList(existingItemList);
        try {
            cartRepository.save(existingCart);
            return existingCart;
        } catch (Exception exception) {
            log.error("service=CartServiceImpl; method=createCart(); message=CART NOT UPDATED");
            throw new UnableToSaveException("Cart did not get updated!");
        }
    }

    //code to create a new cart for the user
    private Cart createCart(CartDTO cartDTO, User user) {
        log.info("service=CartServiceImpl; method=createCart(); message=no cart exists, creating new cart");
        Cart cart = new Cart();
        cart.setEmail(user.getEmail());
        int itemKey = cartDTO.getItemInCart().getItemId();
        int itemValue = cartDTO.getItemInCart().getQuantity();
        Item item = itemRepository.findByItemId(itemKey);
        if (item != null) {
            Map<Integer, Integer> itemList = new HashMap<>();
            itemList.put(itemKey, itemValue);
            cart.setItemList(itemList);
        } else {
            log.error("service=CartServiceImpl; method=createCart(); message=ITEM NOT FOUND");
            throw new ItemNotFoundException("Item not found!");
        }
        try {
            log.info("service=CartServiceImpl; method=createCart(); message=saving the cart");
            cartRepository.save(cart);
            return cart;
        } catch (Exception exception) {
            log.error("service=CartServiceImpl; method=createCart(); message=CART DID NOT GET SAVED");
            throw new UnableToSaveException("Cart not saved!");
        }
    }
}
