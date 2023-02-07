package com.onlineShopping.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin implements Serializable {
    @Id
    private String id;
    @Pattern(regexp = "^([a-zA-Z]+ ?)*$", message = "Please enter valid first name")
    @NotEmpty(message = "Please enter first name.")
    @NotNull
    private String firstName;
    @Pattern(regexp = "^([a-zA-Z]+ ?)*$", message = "Please enter valid last name")
    @NotEmpty(message = "Please enter last name.")
    @NotNull
    private String lastName;
    @NotEmpty(message = "Please enter your phone number.")
    @Size(min = 10, max = 10, message = "Please give the appropriate mobile number")
    private String mobileNo;
    @Email(message = "Please give a valid email")
    @NotEmpty(message = "Please give an email")
    @NotNull
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters long.")
    @NotEmpty(message = "Please enter the password")
    @NotNull
    private String password;
}
