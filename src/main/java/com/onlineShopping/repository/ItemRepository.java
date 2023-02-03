package com.onlineShopping.repository;

import com.onlineShopping.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item, Integer> {

    @Query("{\"category\":\"?0\"}")
    List<Item> itemsByCategory(String category);

    Item findByItemId(int itemId);
}
