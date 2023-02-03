package com.onlineShopping.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineShopping.constants.enums.Category;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "item")
public class Item implements Serializable {

    @Id
    private String id;
    @NotNull
    private int itemId;
    @NotEmpty(message = "Please Enter the item name")
    private String itemName;
    @NotNull(message = "Please enter the price of item")
    private long price;
    @NotNull(message = "Please choose whether to save the item to preference")
    private boolean saveToPreference;
    @NotNull
    @Min(1)
    @Max(50)
    private int quantity;

    private Category category;

    public Item(String id, int itemId, String itemName, long price, boolean saveToPreference, int quantity, Category category) {
        super();
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.saveToPreference = saveToPreference;
        this.quantity = quantity;
        this.category = category;
    }

    public Item() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isSaveToPreference() {
        return saveToPreference;
    }

    public void setSaveToPreference(boolean saveToPreference) {
        this.saveToPreference = saveToPreference;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", saveToPreference=" + saveToPreference +
                ", quantity=" + quantity +
                ", category=" + category +
                '}';
    }
}
