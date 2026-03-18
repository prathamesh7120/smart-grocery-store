package com.grocery.grocery_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @NotBlank(message = "Category is required")
    private String category;

    private String brand;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    private String imageUrl;

    @Min(0) @Max(100)
    private double discountPercent = 0;
}
