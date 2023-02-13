package com.onlineShopping.dto;

import com.onlineShopping.model.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminItemDTO {
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    private Item item;
}
