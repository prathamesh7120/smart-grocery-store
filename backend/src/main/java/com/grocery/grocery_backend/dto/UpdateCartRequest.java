package com.grocery.grocery_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCartRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity; // 0 = remove item
}
