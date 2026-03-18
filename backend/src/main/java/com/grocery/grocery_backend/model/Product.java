package com.grocery.grocery_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private double price;
    private String category;
    private String brand;
    private int stock;
    private String imageUrl;
    private boolean isActive = true;
    private double discountPercent = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Category {
        FRUITS_VEGETABLES,
        DAIRY_EGGS,
        BEVERAGES,
        SNACKS,
        BAKERY,
        HOUSEHOLD
    }
}
