package com.bipinkish.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateItemToCartRequest {
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity shouldn't be negative")
    private Integer quantity;
}
