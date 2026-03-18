package com.grocery.grocery_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotNull(message = "Delivery address is required")
    @Valid
    private AddressDto deliveryAddress;

    @NotBlank(message = "Delivery slot is required")
    private String deliverySlot; // "MORNING" or "EVENING"

    @Data
    public static class AddressDto {

        @NotBlank(message = "Full name is required")
        private String fullName;

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter valid 10-digit phone")
        private String phone;

        @NotBlank(message = "Address line 1 is required")
        private String line1;

        private String line2;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "State is required")
        private String state;

        @NotBlank(message = "Pincode is required")
        @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Enter valid 6-digit pincode")
        private String pincode;
    }
}
