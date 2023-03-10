package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.dto.ItemDTO;
import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.*;
import com.onlineShopping.model.Item;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.repository.OrderRepository;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.OrderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    //code for placing an order
    @Transactional
    @Override
    public Order placeOrder(OrderDTO orderDTO) throws MessagingException, MailException {
        log.info("service=OrderServiceImpl; method=placeOrder(); message=placing orders");
        User user = userRepository.findByEmail(orderDTO.getEmail());
        if (Objects.isNull(user)) {
            log.error("service=OrderServiceImpl; method=placeOrder(); message=USER NOT FOUND");
            throw new UserNotFoundException("User not found, please register and try again!");
        }
        List<Item> items = orderDTO.getItemsToOrder();
        List<ItemDTO> orderItems = new ArrayList<>();
        float totalAmount = 0;

        for (Item item : items) {
            Item fetchedItem = itemRepository.findByItemId(item.getItemId());
            if (fetchedItem == null) {
                log.error("service=OrderServiceImpl; method=placeOrder(); message=ITEM NOT FOUND");
                throw new ItemNotFoundException("Item not found!");
            }
            int currQuantity = fetchedItem.getQuantity();
            //checking if the item is in stock
            if (currQuantity <= 0) {
                log.error("service=OrderServiceImpl; method=placeOrder(); message=ITEM NOT AVAILABLE");
                throw new ItemNotAvailableException("Item not available!");
            }
            //transforming itemDTO
            ItemDTO itemDTO = prepareItemDTO(item, fetchedItem);
            int updatedQuantity = currQuantity - item.getQuantity();
            fetchedItem.setQuantity(updatedQuantity);
            //updating item quantity in itemRepository
            try {
                itemRepository.save(fetchedItem);
                totalAmount += item.getQuantity() * fetchedItem.getPrice();
                orderItems.add(itemDTO);
            } catch (Exception exception) {
                log.error("service=OrderServiceImpl; method=placeOrder(); message=ITEM QUANTITY NOT UPDATED");
                throw new UnableToSaveException("Item quantity could not get updated!");
            }
        }
        //preparing the order
        Order order = prepareOrder(user, orderItems, totalAmount);
        try {
            orderRepository.save(order);
            log.info("service=OrderServiceImpl; method=placeOrder(); message=order placed with {} item(s)", orderItems.size());
        } catch (Exception exception) {
            log.error("service=OrderServiceImpl; method=placeOrder(); message=ORDER NOT SAVED");
            throw new UnableToSaveException("Order did not saved!");
        }
        //send a mail to the user
        orderPlacedMail(order, user);
        return order;
    }

    //code for sending a 'Thank you' mail to the user
    private void orderPlacedMail(Order order, User user) throws MessagingException, MailException {
        log.info("service=OrderServiceImpl; method=successfulOrderMail(); message=preparing mail to send to user");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(user.getEmail());
        helper.setSubject("ONLINE SHOPPING SYSTEM - Thank you for shopping with us!!!");
        helper.setText(
                "Thank you " + user.getFirstName() + ", for shopping with us." +
                        "\nBelow are your shopping details :" +
                        "\nOrder-Id : " + order.getOrderId() +
                        "\nPlaced on : " + order.getOrderDateTime() +
                        "\nName : " + user.getFirstName() + " " + user.getLastName() +
                        "\nAddress : " + user.getAddress() +
                        "\nItems : " + order.getItems().stream().map(itemDTO -> itemDTO.getItemName()).collect(Collectors.toList()) +
                        "\nItem quantity : " + order.getItems().stream().map(itemDTO -> itemDTO.getQuantity()).collect(Collectors.toList()) +
                        "\nPrice of each item : " + order.getItems().stream().map(itemDTO -> itemDTO.getPrice()).collect(Collectors.toList()) +
                        "\nTotal Amount : " + order.getTotalAmount()
        );
        log.info("service=OrderServiceImpl; method=successfulOrderMail(); message=mail sent");
        javaMailSender.send(mimeMessage);
    }

    //code for preparing an item
    private ItemDTO prepareItemDTO(Item item, Item fetchedItem) {
        ItemDTO itemDTO = new ItemDTO();
        log.info("service=OrderServiceImpl; method=prepareItemDTO(); message=transforming itemDTO");
        //adding details of the item retrieved in itemDTO
        if (fetchedItem.getQuantity() < item.getQuantity()) {
            log.error("service=OrderServiceImpl; method=prepareItemDTO(); message=only {} unit(s) available", fetchedItem.getQuantity());
            throw new ItemQuantityNotAvailableException(fetchedItem.getItemName() + " available quantity is only " + fetchedItem.getQuantity());
        }
        itemDTO.setItemId(fetchedItem.getItemId());
        itemDTO.setItemName(fetchedItem.getItemName());
        //adding quantity of item in itemDTO
        itemDTO.setQuantity(item.getQuantity());
        //evaluating price of item by quantity
        itemDTO.setPrice(item.getQuantity() * fetchedItem.getPrice());
        return itemDTO;
    }

    //code for preparing an order
    private Order prepareOrder(User user, List<ItemDTO> orderItems, float totalAmount) {
        Order order = new Order();
        order.setName(user.getFirstName() + " " + user.getLastName());
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setOrderDateTime(LocalDateTime.now());
        order.setMobileNumber(user.getMobileNo());
        order.setAddress(user.getAddress());
        return order;
    }
}
