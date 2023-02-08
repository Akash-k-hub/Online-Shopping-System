package com.onlineShopping.repository;

import com.onlineShopping.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {

    Cart findByEmail(String email);
}
