package com.grocery.grocery_backend.repository;

import com.grocery.grocery_backend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByUserIdOrderByPlacedAtDesc(String userId);

    List<Order> findAllByOrderByPlacedAtDesc();

    List<Order> findByStatus(Order.OrderStatus status);
}
