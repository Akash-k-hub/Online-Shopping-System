package com.onlineShopping.service;

import com.onlineShopping.dto.ItemDTO;
import com.onlineShopping.dto.OrderDTO;
import com.onlineShopping.exception.ItemNotAvailable;
import com.onlineShopping.model.Item;
import com.onlineShopping.model.Order;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.ItemRepository;
import com.onlineShopping.repository.OrderRepository;
import com.onlineShopping.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public User registration(User user) {
        log.info("service=UserServiceImpl; method=registration(); message=saving to repository");
        return userRepository.save(user);
    }

    @Override
    public boolean validateUserLogin(String email, String password) {
        log.info("service=UserServiceImpl; method=validateUserLogin(); message=validating user credentials");
        User validUser = userRepository.validateUserLogin(email, password);
        boolean result = false;
        if(validUser!=null){
            result = true;
        }
        return result;
    }

    @Override
    public Order placeOrder(OrderDTO orderDto) throws NullPointerException, ItemNotAvailable {
        log.info("service=UserServiceImpl; method=placeOrder(); message=placing orders");
        String email = orderDto.getEmail();
        User user = userRepository.findByEmail(email);
        if (Objects.isNull(user)){
            throw new NullPointerException("User not found, please register and try again!");
        }

        List<Item> items = orderDto.getItemsToOrder();
        List<ItemDTO> orderItems = new ArrayList<>();
        int totalAmount = 0;

        for (Item item : items){
            try{
                Item fetchedItem = itemRepository.findByItemId(item.getItemId());
                if(Objects.isNull(fetchedItem)){
                    throw new NullPointerException();
                }
                int currQuantity = fetchedItem.getQuantity();
//                checking if the item is in stock
                if (currQuantity <= 0){
                    throw new ItemNotAvailable();
                }
//                transforming itemDTO
                var itemDTO = prepareItemDTO(item, fetchedItem);
                int updatedQuantity = currQuantity - item.getQuantity();
                fetchedItem.setQuantity(updatedQuantity);
//              updating item quantity in itemRepository
                itemRepository.save(fetchedItem);
                totalAmount += (int) (item.getQuantity()* fetchedItem.getPrice());
                orderItems.add(itemDTO);
            }catch(NullPointerException exception){
                throw new NullPointerException("Item not found!");
            }
            catch (ItemNotAvailable exception){
                throw new ItemNotAvailable("Sorry, Item out of stock! Please check back later.");
            }
        }
//      creating new order
        Order newOrder = new Order();
        newOrder.setName(user.getFirstName());
        newOrder.setEmail(email);
        newOrder.setItems(orderItems);
        newOrder.setTotalAmount(totalAmount);

        orderRepository.save(newOrder);
        log.info("service=UserServiceImpl; method=placeOrder(); message=order placed with {} items", orderItems.size());
        return newOrder;
    }

    private ItemDTO prepareItemDTO(Item item, Item fetchedItem){
        var itemDTO = new ItemDTO();
        log.info("service=UserServiceImpl; method=transformItem(); message=transforming itemDTO");
//        adding details of the item retrieved in itemDTO
        itemDTO.setItemId(fetchedItem.getItemId());
        itemDTO.setItemName(fetchedItem.getItemName());
//        adding quantity of item in itemDTO
        itemDTO.setQuantity(item.getQuantity());
//        evaluating price of item by quantity
        itemDTO.setPrice((int) (item.getQuantity()* fetchedItem.getPrice()));
        return itemDTO;
    }
}
