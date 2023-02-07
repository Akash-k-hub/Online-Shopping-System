package com.onlineShopping.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlineShopping.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order implements Serializable {
    @Id
    private String orderId;
    private LocalDateTime orderDateTime;
    private String name;
    private String address;
    private String mobileNumber;
    private String email;
    private List<ItemDTO> items;
    private float totalAmount;
}
