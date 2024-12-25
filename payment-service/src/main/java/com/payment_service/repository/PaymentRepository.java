package com.payment_service.repository;

import com.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Custom query method to find payments by order ID
    List<Payment> findByOrderId(String orderId);

    // Custom query method to find payments by customer ID
    List<Payment> findByCustomerId(String customerId);
}
