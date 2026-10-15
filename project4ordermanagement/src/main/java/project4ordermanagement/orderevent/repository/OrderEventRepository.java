package project4ordermanagement.orderevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project4ordermanagement.orderevent.entity.OrderEvent;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
}
