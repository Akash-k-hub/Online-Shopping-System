package com.onlineShopping.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "admin")
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
    @Email(message = "Please give valid email-id")
    @NotEmpty(message = "Please give the email-id")
    @NotNull
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters long.")
    @NotEmpty(message = "Please enter the password")
    @NotNull
    private String password;

    public Admin() {
    }

    public Admin(String id, String firstName, String lastName, String mobileNo, String email, String password) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
