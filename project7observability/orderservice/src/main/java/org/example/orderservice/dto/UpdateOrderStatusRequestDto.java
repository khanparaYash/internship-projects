package org.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.example.orderservice.entity.OrderStatus;

@Schema(description = "Requested order status")
public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Order status is required")
        OrderStatus status
) {
}
