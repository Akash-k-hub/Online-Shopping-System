package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.ItemNotAvailableException;
import com.onlineShopping.model.Order;

public interface OrderService {

    Order placeOrder(OrderDTO orderDTO);
}
