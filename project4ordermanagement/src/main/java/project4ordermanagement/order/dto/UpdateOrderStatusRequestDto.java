package project4ordermanagement.order.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import project4ordermanagement.order.entity.OrderStatus;

@Schema(description = "Requested order status")
public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Order status is required")
        OrderStatus status
) {
}
