package project4ordermanagement.orderevent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import project4ordermanagement.order.entity.Order;
import project4ordermanagement.order.entity.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Data
public class OrderEvent {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String payload;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
