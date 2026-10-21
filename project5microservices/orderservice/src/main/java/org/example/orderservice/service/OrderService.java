package org.example.orderservice.service;

import org.example.orderservice.dto.OrderItemRequestDto;
import org.example.orderservice.dto.OrderRequestDto;
import org.example.orderservice.dto.OrderResponseDto;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.OrderStatus;
import org.example.orderservice.exception.InvalidOperationException;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderItemRepository;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
//    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
//    private final OrderEventService orderEventService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderMapper = orderMapper;

    }
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto request) {
        Order newOrder = new Order();
        newOrder.setCustomerEmail(request.customerEmail());
        int totalAmount = 0;
        Order order = orderRepository.save(newOrder);

        for (OrderItemRequestDto item : request.items()) {

            //send request to product service to check if the product is available and get the price

            // ProductSummaryDto ProductSummary= api(item.productId())
//            if(item.quantity() <= ProductSummary.getAvailableQuantity() ){
//                throw new Exception("Insufficient quantity for product ID: " + item.productId());
//            }

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
//        orderEventService.saveEvent(order);
        return orderMapper.toDto(orderRepository.save(order));
    }
    @Transactional
    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOperationException("Payment can only be processed for orders in CREATED status");
        }

//        boolean isPaid = ThreadLocalRandom.current().nextBoolean();
//        Payment payment=new Payment();
//        payment.setOrderId(order);
//        if(isPaid){
            order.setStatus(OrderStatus.PAID);
//            payment.setStatus(PaymentStatus.SUCCESS);
//            paymentRepository.save(payment);
//            orderEventService.saveEvent(order);
            orderRepository.save(order);
//        }else{
//            payment.setStatus(PaymentStatus.FAILED);
//            paymentRepository.save(payment);
//            throw new PaymentFailedException("Payment Failed");
//        }
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOperationException("Only orders in CREATED or PAID status can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
//        orderEventService.saveEvent(order);
        orderRepository.save(order);
    }
}

