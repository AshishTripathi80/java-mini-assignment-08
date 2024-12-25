package com.payment_service.service;

import com.payment_service.entity.Payment;

import java.util.List;

public interface PaymentService {
    Payment savePayment(Payment payment); // Save a payment
    List<Payment> getPaymentsByOrderId(String orderId); // Fetch payments by order ID
    List<Payment> getPaymentsByCustomerId(String customerId); // Fetch payments by customer ID
}
