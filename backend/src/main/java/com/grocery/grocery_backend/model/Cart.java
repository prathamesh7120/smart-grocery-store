package com.grocery.grocery_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private String userId;

    private List<CartItem> items = new ArrayList<>();

    @Data
    public static class CartItem {
        private String productId;
        private String productName;
        private String imageUrl;
        private double price;
        private double discountPercent;
        private int quantity;

        public double getEffectivePrice() {
            return price - (price * discountPercent / 100);
        }
    }

    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(i -> i.getEffectivePrice() * i.getQuantity())
                .sum();
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
