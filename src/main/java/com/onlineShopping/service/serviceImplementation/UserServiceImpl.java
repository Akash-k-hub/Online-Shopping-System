package com.onlineShopping.service.serviceImplementation;

import com.onlineShopping.exception.UnableToSaveException;
import com.onlineShopping.exception.UserNotFoundException;
import com.onlineShopping.model.User;
import com.onlineShopping.repository.UserRepository;
import com.onlineShopping.service.interfaceService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registration(User user) {
        log.info("service=UserServiceImpl; method=registration(); message=saving to repository");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try{
            return userRepository.save(user);
        }catch (Exception exception){
            throw new UnableToSaveException("User not registered!");
        }
    }

    @Override
    public boolean validateUserLogin(String email, String password) {
        log.info("service=UserServiceImpl; method=validateUserLogin(); message=validating user");
        User user = userRepository.findByEmail(email);
        if (user==null){
            throw new UserNotFoundException("Invalid email, please check and try again!");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info("service=UserServiceImpl; method=validateUserLogin(); message=validating hashed password");
        boolean encodedPassword = passwordEncoder.matches(password, user.getPassword());
        boolean result = false;
        if(encodedPassword){
            log.info("service=UserServiceImpl; method=validateUserLogin(); message=credentials authenticated");
            result = true;
        }
        return result;
    }
}
