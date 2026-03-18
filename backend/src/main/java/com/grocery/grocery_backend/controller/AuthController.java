package com.grocery.grocery_backend.controller;

import com.grocery.grocery_backend.dto.ApiResponse;
import com.grocery.grocery_backend.dto.LoginRequest;
import com.grocery.grocery_backend.dto.RegisterRequest;
import com.grocery.grocery_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> data = authService.login(request);
        return ResponseEntity.ok(new ApiResponse(true, "Login successful", data));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam String email,
                                                   @RequestParam String otp) {
        String message = authService.verifyEmail(email, otp);
        return ResponseEntity.ok(new ApiResponse(true, message));
    }
}
