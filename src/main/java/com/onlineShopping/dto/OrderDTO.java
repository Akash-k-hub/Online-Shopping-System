package com.onlineShopping.dto;

import com.onlineShopping.model.Item;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String email;

    private List<Item> itemsToOrder;
}
