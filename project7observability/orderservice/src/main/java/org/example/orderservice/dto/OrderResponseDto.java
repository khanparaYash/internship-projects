package org.example.orderservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.orderservice.entity.OrderStatus;

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
