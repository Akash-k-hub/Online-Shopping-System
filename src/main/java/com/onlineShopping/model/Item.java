package com.onlineShopping.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineShopping.constants.enums.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
    @Id
    private String id;
    @NotNull
    private int itemId;
    @NotEmpty(message = "Please Enter the item name")
    private String itemName;
    @NotNull(message = "Please enter the price of item")
    private float price;
    @NotNull
    @Min(1)
    @Max(50)
    private int quantity;
    @NotNull
    private Category category;
}
