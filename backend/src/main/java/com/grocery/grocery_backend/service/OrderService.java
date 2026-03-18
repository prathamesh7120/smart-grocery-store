package com.grocery.grocery_backend.service;

import com.grocery.grocery_backend.dto.PlaceOrderRequest;
import com.grocery.grocery_backend.model.Cart;
import com.grocery.grocery_backend.model.Order;
import com.grocery.grocery_backend.model.Product;
import com.grocery.grocery_backend.repository.CartRepository;
import com.grocery.grocery_backend.repository.OrderRepository;
import com.grocery.grocery_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    // ─── Place order ──────────────────────────────────────

    public Order placeOrder(String userEmail, PlaceOrderRequest request) {

        // 1. Get cart
        Cart cart = cartRepository.findByUserId(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }

        // 2. Build order items + validate stock
        List<Order.OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;
        double discountAmount = 0;

        for (Cart.CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + cartItem.getProductName()));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for: " + product.getName()
                                + ". Available: " + product.getStock());
            }

            double originalPrice = cartItem.getPrice() * cartItem.getQuantity();
            double discount      = originalPrice * cartItem.getDiscountPercent() / 100;
            double itemTotal     = originalPrice - discount;

            Order.OrderItem orderItem = new Order.OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setImageUrl(cartItem.getImageUrl());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setDiscountPercent(cartItem.getDiscountPercent());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setItemTotal(itemTotal);

            orderItems.add(orderItem);
            totalAmount    += originalPrice;
            discountAmount += discount;

            // 3. Reduce stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 4. Build address
        Order.Address address = new Order.Address();
        PlaceOrderRequest.AddressDto dto = request.getDeliveryAddress();
        address.setFullName(dto.getFullName());
        address.setPhone(dto.getPhone());
        address.setLine1(dto.getLine1());
        address.setLine2(dto.getLine2());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());

        // 5. Create order
        Order order = new Order();
        order.setUserId(userEmail);
        order.setUserEmail(userEmail);
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(totalAmount - discountAmount);
        order.setDeliveryAddress(address);
        order.setDeliverySlot(request.getDeliverySlot());

        Order savedOrder = orderRepository.save(order);

        // 6. Clear cart after order
        cart.getItems().clear();
        cartRepository.save(cart);

        // 7. Send confirmation email
        emailService.sendOrderConfirmation(
                userEmail,
                savedOrder.getId(),
                savedOrder.getFinalAmount());

        return savedOrder;
    }

    // ─── Customer: my orders ──────────────────────────────

    public List<Order> getMyOrders(String userEmail) {
        return orderRepository.findByUserIdOrderByPlacedAtDesc(userEmail);
    }

    public Order getOrderById(String userEmail, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userEmail)) {
            throw new RuntimeException("Access denied");
        }

        return order;
    }

    public Order cancelOrder(String userEmail, String orderId) {
        Order order = getOrderById(userEmail, orderId);

        if (order.getStatus() != Order.OrderStatus.PLACED) {
            throw new RuntimeException(
                    "Order cannot be cancelled. Current status: "
                            + order.getStatus());
        }

        // Restore stock
        for (Order.OrderItem item : order.getItems()) {
            productRepository.findById(item.getProductId()).ifPresent(p -> {
                p.setStock(p.getStock() + item.getQuantity());
                productRepository.save(p);
            });
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // ─── Admin ────────────────────────────────────────────

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByPlacedAtDesc();
    }

    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status
                    + ". Valid values: PLACED, CONFIRMED, PREPARING, "
                    + "OUT_FOR_DELIVERY, DELIVERED, CANCELLED");
        }

        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
