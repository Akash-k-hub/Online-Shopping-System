package com.onlineShopping.service.interfaceService;

import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.ItemNotAvailableException;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.JavaMailSender;

public interface OrderService {

//    void mailService(JavaMailSender javaMailSender);
    Order placeOrder(OrderDTO orderDTO) throws MessagingException;

    void successfulOrderMail(Order order, User user) throws MessagingException;
}
