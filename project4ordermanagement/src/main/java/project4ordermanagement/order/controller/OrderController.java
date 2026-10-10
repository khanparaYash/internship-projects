package project4ordermanagement.order.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project4ordermanagement.order.dto.OrderRequestDto;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.dto.UpdateOrderStatusRequestDto;
import project4ordermanagement.order.service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Create, retrieve, update, pay for, and cancel orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create an order")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto request) {
        return ResponseEntity.status(201).body(orderService.createOrder(request));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get an order by ID")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update an order's status")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request
    ) {
        return  ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.status()));
    }
    @PostMapping("/{orderId}/payment")
    @Operation(summary = "Process payment for an order")
    public ResponseEntity<String> payment(@PathVariable Long orderId) {
        orderService.processPayment(orderId);
        return ResponseEntity.ok( "Payment processed successfully for order " + orderId);
    }

    @Operation(summary = "Cancel an order")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

}


