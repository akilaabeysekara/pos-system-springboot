package lk.ijse.posbackend.repository;

import lk.ijse.posbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Payment findTopByOrderByPaymentIdDesc();
}