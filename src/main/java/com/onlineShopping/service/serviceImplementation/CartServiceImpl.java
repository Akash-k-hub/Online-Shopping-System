package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.dto.CartDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Cart addToCart(CartDTO cartDTO) {
        log.info("service=CartServiceImpl; method=addToCart(); message=checking if user is registered");
        //check if user is registered or not
        User user = userRepository.findByEmail(cartDTO.getEmail());
        if (user != null) {
            Cart existingCart = cartRepository.findByEmail(cartDTO.getEmail());
            //check if user already has a cart
            log.info("service=CartServiceImpl; method=addToCart(); message=checking if user already has a cart");
            if (existingCart != null) {
                //update the cart
                Cart cart = updateCart(existingCart, cartDTO);
                return cart;
            } else {
                //create a cart
                Cart cart = createCart(cartDTO, user);
                return cart;
            }
        }
        throw new UserNotFoundException("User not registered, please register and try again!");
    }

    @Override
    public boolean removeItemFromCart(CartDTO cartDTO) {
        log.info("service=CartServiceImpl; method=removeItemFromCart(); message=checking if user is registered");
        //check if user is registered
        if (userRepository.findByEmail(cartDTO.getEmail()) != null) {
            Cart cart = cartRepository.findByEmail(cartDTO.getEmail());
            log.info("service=CartServiceImpl; method=removeItemFromCart(); message=checking if cart is empty");
            if (cart != null) {
                List<Item> itemsInCart = cart.getItemsInCart();
                ListIterator<Item> listIterator = itemsInCart.listIterator();
                try {
                    for (Item item : itemsInCart) {
                        if (item.getItemId() == cartDTO.getItemInCart().getItemId()) {
                            listIterator.remove();
                        }
                    }
                    cart.setItemsInCart(itemsInCart);
                    log.info("service=CartServiceImpl; method=removeItemFromCart(); message=updating the cart");
                    cartRepository.save(cart);
                    return true;
                } catch (ItemNotFoundException exception) {
                    throw new ItemNotFoundException("Item not found!");
                } catch (Exception exception) {
                    throw new UnableToSaveException("Cart did not get saved!");
                }
            }
            throw new CartNotFoundException("Cart not found!");
        }
        throw new UserNotFoundException("User not registered, please register and try again!");
    }

    private Cart updateCart(Cart existingCart, CartDTO cartDTO) {
        //check if cart already have that item -> update the quantity
        List<Item> existingItemList = existingCart.getItemsInCart();
        ListIterator<Item> listIterator = existingItemList.listIterator();
        while (listIterator.hasNext()) {
            Item item = listIterator.next();
            //updating item quantity in the cart
            if (item.getItemId() == cartDTO.getItemInCart().getItemId()) {
                log.info("service=CartServiceImpl; method=createCart(); message=updating item in the cart");
                item.setQuantity(cartDTO.getItemInCart().getQuantity());
            }
        }
        if (!existingItemList.contains(cartDTO)) {
            Item addItem = new Item();
            addItem.setItemId(cartDTO.getItemInCart().getItemId());
            addItem.setQuantity(cartDTO.getItemInCart().getQuantity());
            existingItemList.add(addItem);
        }
        existingCart.setItemsInCart(existingItemList);
        try {
            cartRepository.save(existingCart);
            return existingCart;
        } catch (Exception exception) {
            throw new UnableToSaveException("Cart did not get updated!");
        }
    }

    private Cart createCart(CartDTO cartDTO, User user) {
        log.info("service=CartServiceImpl; method=createCart(); message=no cart exists, creating new cart");
        Cart cart = new Cart();
        cart.setEmail(user.getEmail());
        List<Item> itemList = new ArrayList<>();
        itemList.add(cartDTO.getItemInCart());
        cart.setItemsInCart(itemList);
        try {
            log.info("service=CartServiceImpl; method=createCart(); message=saving the cart");
            cartRepository.save(cart);
            return cart;
        } catch (Exception exception) {
            log.warn("service=CartServiceImpl; method=createCart(); message=cart did not saved");
            throw new UnableToSaveException("Cart not saved!");
        }
    }
}
