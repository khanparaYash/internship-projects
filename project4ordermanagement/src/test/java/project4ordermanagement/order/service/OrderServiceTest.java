package project4ordermanagement.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import project4ordermanagement.exception.InvalidOperationException;
import project4ordermanagement.exception.ResourceNotFoundException;
import project4ordermanagement.order.dto.OrderItemRequestDto;
import project4ordermanagement.order.dto.OrderRequestDto;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.entity.Order;
import project4ordermanagement.order.entity.OrderStatus;
import project4ordermanagement.order.mapper.OrderMapperImpl;
import project4ordermanagement.order.repository.OrderItemRepository;
import project4ordermanagement.order.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Use @DataJpaTest for JPA repository testing
@Import({OrderService.class, OrderMapperImpl.class}) // Import the service and mapper for testing
class OrderServiceTest {
    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  OrderItemRepository orderItemRepository;
    @Autowired
    private  OrderService orderService;



    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

    @Test
    void createOrder_shouldSaveOrderAndItemsAndReturnMappedResponse() {
        OrderRequestDto request = new OrderRequestDto(
                "customer@example.com",
                List.of(
                        new OrderItemRequestDto(101L, 2, 50),
                        new OrderItemRequestDto(202L, 1, 75)
                )
        );

        OrderResponseDto response = orderService.createOrder(request);

        assertEquals("customer@example.com", response.customerEmail());
        assertEquals(OrderStatus.CREATED, response.status());
        assertEquals(175, response.totalAmount());
        assertEquals(2, orderItemRepository.findAll().size());
        assertEquals(1, orderRepository.findAll().size());
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setTotalAmount(100);
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        OrderResponseDto result = orderService.getOrderById(order.getId());

        assertNotNull(result);
        assertEquals("customer@example.com", result.customerEmail());
        assertEquals(100, result.totalAmount());
    }

    @Test
    void getOrderById_shouldThrowResourceNotFoundException_whenOrderMissing() {
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(999L)
        );

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void updateOrderStatus_shouldChangeStatusForValidTransition() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        OrderResponseDto response = orderService.updateOrderStatus(order.getId(), OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, response.status());
        assertEquals(OrderStatus.PAID, orderRepository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void updateOrderStatus_shouldThrowInvalidOperationException_forInvalidTransition() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> orderService.updateOrderStatus(order.getId(), OrderStatus.SHIPPED)
        );

        assertTrue(exception.getMessage().contains("Invalid order status transition"));
    }

    @Test
    void processPayment_shouldMarkOrderAsPaid_whenStatusIsCreated() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        orderService.processPayment(order.getId());

        assertEquals(OrderStatus.PAID, orderRepository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void processPayment_shouldThrow_whenOrderNotCreated() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> orderService.processPayment(order.getId())
        );

        assertEquals("Payment can only be processed for orders in CREATED status", exception.getMessage());
    }

    @Test
    void cancelOrder_shouldMarkOrderCancelled_forCreatedOrPaidStatus() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        orderService.cancelOrder(order.getId());

        assertEquals(OrderStatus.CANCELLED, orderRepository.findById(order.getId()).orElseThrow().getStatus());
    }

    @Test
    void cancelOrder_shouldThrow_whenOrderStatusIsShipped() {
        Order order = new Order();
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> orderService.cancelOrder(order.getId())
        );

        assertEquals("Only orders in CREATED or PAID status can be cancelled", exception.getMessage());
    }
}
