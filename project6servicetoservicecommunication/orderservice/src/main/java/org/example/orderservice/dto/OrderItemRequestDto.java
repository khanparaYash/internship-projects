package org.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Product and pricing details for an order item")
public record OrderItemRequestDto(
        @Schema(description = "Product identifier", example = "101", minimum = "1")
        @Min(value = 1, message = "Product ID must be a positive number")
        @Column(nullable = false)
        @NotNull
        Long productId,

        @Schema(description = "Number of units", example = "2", minimum = "1")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Column(nullable = false)
        @NotNull
        int quantity
) {
}
