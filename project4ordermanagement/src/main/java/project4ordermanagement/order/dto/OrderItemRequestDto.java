package project4ordermanagement.order.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product and pricing details for an order item")
public record OrderItemRequestDto(
        @Schema(description = "Product identifier", example = "101", minimum = "1")
        @Min(value = 1, message = "Product ID must be a positive number")
        @Column(nullable = false)
        Long productId,

        @Schema(description = "Number of units", example = "2", minimum = "1")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Column(nullable = false)
        int quantity,

        @Schema(description = "Unit price", example = "1999", minimum = "0")
        @Min(value = 0, message = "Unit price must be non-negative")
        @Column(nullable = false)
        int unitPrice
) {
}
