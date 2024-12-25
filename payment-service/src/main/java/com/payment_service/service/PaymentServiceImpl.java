package com.payment_service.service;

import com.payment_service.entity.Payment;
import com.payment_service.exception.ResourceNotFoundException;
import com.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @Override
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPaymentsByOrderId(String orderId) {
        try {
            Thread.sleep(5000); // 5-second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted", e);
        }

        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("No payments found for order ID: " + orderId);
        }
        return payments;
    }


    @Override
    public List<Payment> getPaymentsByCustomerId(String customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }
}
