package org.example.orderservice.controller;

import com.example.api.OrdersApi;
import com.example.dto.OrderRequestDto;
import com.example.dto.OrderResponseDto;
import com.example.dto.UpdateOrderStatusRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class OrderController implements OrdersApi {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

   @Override
    public ResponseEntity<OrderResponseDto> createOrder(OrderRequestDto request) {
        return ResponseEntity.status(201).body(orderService.createOrder(request));
    }

   @Override
    public ResponseEntity<OrderResponseDto> getOrderById(Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Override
    public ResponseEntity<OrderResponseDto> updateStatus(Long orderId, UpdateOrderStatusRequestDto request) {
        return  ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderMapper.mapDtoToEntity(request.getStatus()) ));
    }
    @Override
    public ResponseEntity<String> payment( Long orderId) {
        orderService.processPayment(orderId);
        return ResponseEntity.ok( "Payment processed successfully for order " + orderId);
    }

    @Override
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

}


