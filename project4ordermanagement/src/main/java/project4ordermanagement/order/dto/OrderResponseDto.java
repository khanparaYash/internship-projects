package project4ordermanagement.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import project4ordermanagement.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Order details returned by the API")
public record OrderResponseDto(
        Long id,
        String customerEmail,
        OrderStatus status,
        int totalAmount,
        List<OrderItemResponseDto> products,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {

}
