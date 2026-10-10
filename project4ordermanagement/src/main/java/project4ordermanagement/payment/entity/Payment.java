package project4ordermanagement.payment.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import io.swagger.v3.oas.annotations.media.Schema;
import project4ordermanagement.order.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Payment record associated with an order")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orderId;

    @UuidGenerator
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime paidAt;
}
