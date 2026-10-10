package project4ordermanagement.order.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Customer and item details used to create an order")
public record OrderRequestDto(
        @Schema(description = "Customer email address", example = "customer@example.com")
        @NotNull(message = "Customer email is required")
        String customerEmail,
        @Schema(description = "Products included in the order")
        List<OrderItemRequestDto> items
) {
}
