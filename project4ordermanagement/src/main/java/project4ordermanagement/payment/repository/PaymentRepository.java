package project4ordermanagement.payment.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project4ordermanagement.payment.entity.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment,Long> {
}
