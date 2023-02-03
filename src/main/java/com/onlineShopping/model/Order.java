package com.onlineShopping.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineShopping.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    @Id
    private String orderId;
    private String name;
    private String email;
    private List<ItemDTO> items;
    private int totalAmount;
}
