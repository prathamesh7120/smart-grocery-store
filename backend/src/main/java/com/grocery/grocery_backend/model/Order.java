package com.grocery.grocery_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;
    private String userEmail;

    private List<OrderItem> items;

    private double totalAmount;
    private double discountAmount;
    private double finalAmount;

    private Address deliveryAddress;

    private OrderStatus status = OrderStatus.PLACED;

    private String deliverySlot;

    private LocalDateTime placedAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ─── Nested classes ───────────────────────────────────

    @Data
    public static class OrderItem {
        private String productId;
        private String productName;
        private String imageUrl;
        private double price;
        private double discountPercent;
        private int quantity;
        private double itemTotal;
    }

    @Data
    public static class Address {
        private String fullName;
        private String phone;
        private String line1;
        private String line2;
        private String city;
        private String state;
        private String pincode;
    }

    public enum OrderStatus {
        PLACED,
        CONFIRMED,
        PREPARING,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED
    }
}
