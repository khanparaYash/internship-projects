package project4ordermanagement.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project4ordermanagement.exception.InvalidOperationException;
import project4ordermanagement.exception.PaymentFailedException;
import project4ordermanagement.exception.ResourceNotFoundException;
import project4ordermanagement.order.dto.OrderRequestDto;
import project4ordermanagement.order.dto.OrderItemRequestDto;
import project4ordermanagement.order.dto.OrderResponseDto;
import project4ordermanagement.order.entity.*;
import project4ordermanagement.order.mapper.OrderMapper;
import project4ordermanagement.order.repository.OrderItemRepository;
import project4ordermanagement.order.repository.OrderRepository;
import project4ordermanagement.payment.repository.PaymentRepository;
import project4ordermanagement.payment.entity.Payment;
import project4ordermanagement.payment.entity.PaymentStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper,PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentRepository=paymentRepository;
        this.orderMapper = orderMapper;
    }
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto request) {
        Order newOrder = new Order();
        newOrder.setCustomerEmail(request.customerEmail());
        int totalAmount = 0;
        Order order = orderRepository.save(newOrder);

        for (OrderItemRequestDto item : request.items()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.productId());
            orderItem.setQuantity(item.quantity());
            orderItem.setUnitPrice(item.unitPrice());
            orderItem.setLineTotal(((long) item.quantity() * item.unitPrice()));
            totalAmount += orderItem.getLineTotal();
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }
        order.setTotalAmount(totalAmount);
        return orderMapper.toDto(orderRepository.save(order));

    }

    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException( "Order not found"));
        return orderMapper.toDto(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidOperationException(
                    "Invalid order status transition from " + order.getStatus() + " to " + newStatus
            );
        }
        order.setStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }
    @Transactional
    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOperationException("Payment can only be processed for orders in CREATED status");
        }

        boolean isPaid = ThreadLocalRandom.current().nextBoolean();
        Payment payment=new Payment();
        payment.setOrderId(order);
        if(isPaid){
            order.setStatus(OrderStatus.PAID);
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
            orderRepository.save(order);
        }else{
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentFailedException("Payment Failed");
        }
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOperationException("Only orders in CREATED or PAID status can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}

