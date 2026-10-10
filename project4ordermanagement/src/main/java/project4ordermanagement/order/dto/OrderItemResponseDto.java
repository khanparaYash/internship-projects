package project4ordermanagement.order.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product details and calculated totals for an order item")
public record OrderItemResponseDto(

        Long id,
        Long productId,

        @Min(value = 1, message = "Quantity must be at least 1")
        @Column(nullable = false)
        int quantity,

        @Min(value = 0, message = "Unit price must be non-negative")
        @Column(nullable = false)
        int unitPrice,

        @Column(nullable = false)
        Long lineTotal
) {
}
