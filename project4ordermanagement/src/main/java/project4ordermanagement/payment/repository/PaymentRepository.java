package project4ordermanagement.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project4ordermanagement.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
