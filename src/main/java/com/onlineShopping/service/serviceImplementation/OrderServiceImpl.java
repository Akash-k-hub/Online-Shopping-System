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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public Order placeOrder(OrderDTO orderDTO){
        log.info("service=OrderServiceImpl; method=placeOrder(); message=placing orders");
        User user = userRepository.findByEmail(orderDTO.getEmail());
        if (Objects.isNull(user)){
            throw new UserNotFoundException("User not found, please register and try again!");
        }
        List<Item> items = orderDTO.getItemsToOrder();
        List<ItemDTO> orderItems = new ArrayList<>();
        float totalAmount = 0;

        for (Item item : items){
            Item fetchedItem = itemRepository.findByItemId(item.getItemId());
            if(fetchedItem==null){
                throw new ItemNotFoundException("Item not found!");
            }
            int currQuantity = fetchedItem.getQuantity();
            //checking if the item is in stock
            if (currQuantity <= 0){
                throw new ItemNotAvailableException("Item not available!");
            }
            //transforming itemDTO
            ItemDTO itemDTO = prepareItemDTO(item, fetchedItem);
            //optimistic locking
            int updatedQuantity = currQuantity - item.getQuantity();
            fetchedItem.setQuantity(updatedQuantity);
            //updating item quantity in itemRepository
            try{
                itemRepository.save(fetchedItem);

                totalAmount += item.getQuantity()* fetchedItem.getPrice();
                orderItems.add(itemDTO);
            }catch(Exception exception){
                throw new UnableToSaveException("Item quantity could not get updated!");
            }
        }
        //preparing the order
        Order order = prepareOrder(user, orderItems, totalAmount);

        try {
            orderRepository.save(order);
            log.info("service=OrderServiceImpl; method=placeOrder(); message=order placed with {} item(s)", orderItems.size());
        }catch (Exception exception){
            log.warn("ORDER NOT SAVED");
            throw new UnableToSaveException("Order did not saved!");
        }
        return order;
    }

    private ItemDTO prepareItemDTO(Item item, Item fetchedItem){
        var itemDTO = new ItemDTO();
        log.info("service=OrderServiceImpl; method=prepareItemDTO(); message=transforming itemDTO");
        //adding details of the item retrieved in itemDTO
        if (fetchedItem.getQuantity() < item.getQuantity()){
            log.warn("service=OrderServiceImpl; method=prepareItemDTO(); message=only {} unit(s) available", fetchedItem.getQuantity());
            throw new ItemQuantityNotAvailableException(fetchedItem.getItemName() + " available quantity is only " + fetchedItem.getQuantity());
        }
        itemDTO.setItemId(fetchedItem.getItemId());
        itemDTO.setItemName(fetchedItem.getItemName());
        //adding quantity of item in itemDTO
        itemDTO.setQuantity(item.getQuantity());
        //evaluating price of item by quantity
        itemDTO.setPrice(item.getQuantity()* fetchedItem.getPrice());
        return itemDTO;
    }

    private Order prepareOrder(User user, List<ItemDTO> orderItems, float totalAmount){
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
