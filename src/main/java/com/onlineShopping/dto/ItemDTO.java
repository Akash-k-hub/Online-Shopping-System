package com.onlineShopping.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineShopping.constants.enums.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {
    @NotNull
    private int itemId;
    @NotEmpty(message = "Please enter the item name")
    @NotNull(message = "Please enter the item name")
    private String itemName;
    @NotNull
    @Min(1)
    @Max(50)
    private int quantity;
    private float price;
    @NotNull
    private Category category;
}
