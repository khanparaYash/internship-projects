package project4ordermanagement.orderevent.service;

import org.springframework.stereotype.Service;
import project4ordermanagement.order.entity.Order;
import project4ordermanagement.orderevent.entity.OrderEvent;
import project4ordermanagement.orderevent.repository.OrderEventRepository;

@Service
public class OrderEventService {

    private final OrderEventRepository orderEventRepository;

    public OrderEventService(OrderEventRepository orderEventRepository){
        this.orderEventRepository = orderEventRepository;
    }


    public void saveEvent(Order order) {
        OrderEvent event = new OrderEvent();
        event.setOrder(order);
        event.setOrderStatus(order.getStatus());
        event.setPayload("Order "+order.getId()+" status changed to "+order.getStatus().toString());
        orderEventRepository.save(event);
    }
}
