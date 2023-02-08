package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.CartDTO;
import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.model.Cart;

public interface CartService {
    Cart addToCart(CartDTO cartDTO);

    boolean removeItemFromCart(CartDTO cartDTO);
}
