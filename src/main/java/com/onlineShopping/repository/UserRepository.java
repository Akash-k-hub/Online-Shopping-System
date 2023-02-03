package com.onlineShopping.repository;

import com.onlineShopping.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{\"email\":\"?0\",\"password\":\"?1\"}")
    User validateUserLogin(String email, String password);

    User findByEmail(String email);
}
