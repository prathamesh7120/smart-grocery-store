package com.grocery.grocery_backend.controller;

import com.grocery.grocery_backend.dto.ApiResponse;
import com.grocery.grocery_backend.dto.PlaceOrderRequest;
import com.grocery.grocery_backend.dto.UpdateOrderStatusRequest;
import com.grocery.grocery_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ─── Customer endpoints ───────────────────────────────

    @PostMapping("/api/orders")
    public ResponseEntity<ApiResponse> placeOrder(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody PlaceOrderRequest request) {
        var order = orderService.placeOrder(email, request);
        return ResponseEntity.ok(
                new ApiResponse(true, "Order placed successfully", order));
    }

    @GetMapping("/api/orders/my")
    public ResponseEntity<ApiResponse> getMyOrders(
            @AuthenticationPrincipal String email) {
        var orders = orderService.getMyOrders(email);
        return ResponseEntity.ok(
                new ApiResponse(true, "Orders fetched", orders));
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<ApiResponse> getOrderById(
            @AuthenticationPrincipal String email,
            @PathVariable String id) {
        var order = orderService.getOrderById(email, id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Order fetched", order));
    }

    @PutMapping("/api/orders/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(
            @AuthenticationPrincipal String email,
            @PathVariable String id) {
        var order = orderService.cancelOrder(email, id);
        return ResponseEntity.ok(
                new ApiResponse(true, "Order cancelled", order));
    }

    // ─── Admin endpoints ──────────────────────────────────

    @GetMapping("/api/admin/orders")
    public ResponseEntity<ApiResponse> getAllOrders() {
        var orders = orderService.getAllOrders();
        return ResponseEntity.ok(
                new ApiResponse(true, "All orders fetched", orders));
    }

    @PutMapping("/api/admin/orders/{id}/status")
    public ResponseEntity<ApiResponse> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        var order = orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok(
                new ApiResponse(true, "Order status updated", order));
    }
}
