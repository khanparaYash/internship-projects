package project4ordermanagement.order.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project4ordermanagement.exception.InvalidOperationException;
import project4ordermanagement.exception.ResourceNotFoundException;
import project4ordermanagement.order.dto.OrderItemRequestDto;
import project4ordermanagement.order.dto.OrderRequestDto;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.entity.Order;
import project4ordermanagement.order.entity.OrderItem;
import project4ordermanagement.order.entity.OrderStatus;
import project4ordermanagement.order.mapper.OrderMapperImpl;
import project4ordermanagement.order.repository.OrderItemRepository;
import project4ordermanagement.order.repository.OrderRepository;
import project4ordermanagement.payment.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderMapperImpl orderMapper;
    @InjectMocks
    private OrderService orderService;


    @Test
    void createOrder_shouldSaveOrderAndItemsAndReturnMappedResponse() {
        OrderRequestDto request = new OrderRequestDto("customer@example.com", List.of(new OrderItemRequestDto(101L, 2, 50), new OrderItemRequestDto(202L, 1, 75)));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new OrderResponseDto(order.getId(), order.getCustomerEmail(), order.getStatus(), order.getTotalAmount(), null, null, null);
        });

        OrderResponseDto response = orderService.createOrder(request);

        assertEquals("customer@example.com", response.customerEmail());
        assertEquals(OrderStatus.CREATED, response.status());
        assertEquals(175, response.totalAmount());

        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
    }

    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setTotalAmount(100);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
            Order orderResponse = invocation.getArgument(0);
            return new OrderResponseDto(orderResponse.getId(), orderResponse.getCustomerEmail(), orderResponse.getStatus(), orderResponse.getTotalAmount(), null, null, null);
        });


        OrderResponseDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("customer@example.com", result.customerEmail());
        assertEquals(100, result.totalAmount());

        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_shouldThrowResourceNotFoundException_whenOrderMissing() {

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(999L));

        assertEquals("Order not found", exception.getMessage());

        verify(orderRepository).findById(999L);
    }

    @Test
    void updateOrderStatus_shouldChangeStatusForValidTransition() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
            Order orderResponse = invocation.getArgument(0);
            return new OrderResponseDto(orderResponse.getId(), orderResponse.getCustomerEmail(), orderResponse.getStatus(), orderResponse.getTotalAmount(), null, null, null);
        });

        OrderResponseDto response = orderService.updateOrderStatus(1L, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, response.status());
        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_shouldThrowInvalidOperationException_forInvalidTransition() {


        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));


        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.SHIPPED));

        assertTrue(exception.getMessage().contains("Invalid order status transition"));

        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void processPayment_shouldMarkOrderAsPaid_whenStatusIsCreated() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));


        orderService.processPayment(1L);

        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void processPayment_shouldThrow_whenOrderNotCreated() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> orderService.processPayment(1L));

        assertEquals("Payment can only be processed for orders in CREATED status", exception.getMessage());

        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void cancelOrder_shouldMarkOrderCancelled_forCreatedOrPaidStatus() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void cancelOrder_shouldThrow_whenOrderStatusIsShipped() {
        Order order = new Order();
        order.setId(1L);
        order.setCustomerEmail("customer@example.com");
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> orderService.cancelOrder(1L));

        assertEquals("Only orders in CREATED or PAID status can be cancelled", exception.getMessage());

        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));

    }
}
