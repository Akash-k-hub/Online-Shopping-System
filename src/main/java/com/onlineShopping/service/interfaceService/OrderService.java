package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.model.Order;
import jakarta.mail.MessagingException;

public interface OrderService {

    Order placeOrder(OrderDTO orderDTO) throws MessagingException;

}
