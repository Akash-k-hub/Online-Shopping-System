package com.onlineShopping.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "user")
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

    private List<Item> preferences;

    public User() {
    }

    public User(String id, String firstName, String lastName, String gender, String mobileNo, String email, String password, List<Item> preferences) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.mobileNo = mobileNo;
        this.email = email;
        this.password = password;
        this.preferences = preferences;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public List<Item> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Item> preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", preferences=" + preferences +
                '}';
    }
}
