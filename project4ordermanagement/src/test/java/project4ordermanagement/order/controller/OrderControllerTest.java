package project4ordermanagement.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.entity.OrderStatus;
import project4ordermanagement.order.service.OrderService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;  //like a Fake browser to test the controller endpoints

    @MockitoBean // Use @MockitoBean to mock the OrderService bean in the Spring context
    private OrderService orderService;

    @Test
    void createOrder_returnsCreatedOrder() throws Exception {
        when(orderService.createOrder(any())).thenReturn(orderResponse());

        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("""
                                {
                                  "customerEmail": "customer@example.com",
                                  "items": [{"productId": 101, "quantity": 2, "unitPrice": 50}]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerEmail").value("customer@example.com"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalAmount").value(100));

        verify(orderService).createOrder(any());
    }

    @Test
    void createOrder_returnsBadRequestWhenCustomerEmailIsMissing() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("""
                                {"items": []}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer email is required"));

        verifyNoInteractions(orderService);
    }

    @Test
    void getOrderById_returnsOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderResponse());

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerEmail").value("customer@example.com"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    void getAllOrders_returnsOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderResponse()));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CREATED"));

        verify(orderService).getAllOrders();
    }

    @Test
    void updateStatus_returnsUpdatedOrder() throws Exception {
        OrderResponseDto paidOrder = new OrderResponseDto(
                1L, "customer@example.com", OrderStatus.PAID, 100, List.of(), null, null
        );
        when(orderService.updateOrderStatus(1L, OrderStatus.PAID)).thenReturn(paidOrder);

        mockMvc.perform(patch("/orders/1/status")
                        .contentType("application/json")
                        .content("""
                                {"status": "PAID"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        verify(orderService).updateOrderStatus(1L, OrderStatus.PAID);
    }

    @Test
    void payment_processesPaymentAndReturnsConfirmation() throws Exception {
        mockMvc.perform(post("/orders/1/payment"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processed successfully for order 1"));

        verify(orderService).processPayment(1L);
    }

    @Test
    void cancelOrder_cancelsOrderAndReturnsConfirmation() throws Exception {
        mockMvc.perform(post("/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));

        verify(orderService).cancelOrder(1L);
    }

    private OrderResponseDto orderResponse() {
        return new OrderResponseDto(
                1L, "customer@example.com", OrderStatus.CREATED, 100, List.of(), null, null
        );
    }
}
