package com.onlineShopping.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    private String id;
    @Pattern(regexp = "^([a-zA-Z]+ ?)*$", message = "Please enter valid first name")
    @NotEmpty(message = "Please enter first name")
    @NotNull
    private String firstName;
    @Pattern(regexp = "^([a-zA-Z]+ ?)*$", message = "Please enter valid last name")
    @NotEmpty(message = "Please enter last name")
    @NotNull
    private String lastName;
    @NotEmpty
    private String gender;
    @NotEmpty(message = "Please give the contact number")
    @Size(min = 10, max = 10, message = "Please give the appropriate mobile number")
    private String mobileNo;
    @Email
    @NotEmpty(message = "Please give the email")
    @NotNull
    @Indexed(unique = true)
    private String email;
    @NotEmpty
    @Size(min = 8, message = "password should have at least 8 characters")
    @NotNull
    private String password;
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String address;
}
