package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.CartDTO;
import com.onlineShopping.model.Cart;

public interface CartService {
    Cart addItemToCart(CartDTO cartDTO);

    boolean removeItemFromCart(CartDTO cartDTO);

    Cart getCartByEmail(String email);
}
