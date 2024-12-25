package com.payment_service.service;

import com.payment_service.entity.Payment;
import com.payment_service.exception.ResourceNotFoundException;
import com.payment_service.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void testGetPaymentsByOrderId_Success() {
        String orderId = "OR123";
        List<Payment> mockPayments = List.of(
                new Payment(1L, "CU123", orderId, 100.0, "USD", "SUCCESS", null)
        );

        when(paymentRepository.findByOrderId(orderId)).thenReturn(mockPayments);

        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);

        assertEquals(1, payments.size());
        verify(paymentRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void testGetPaymentsByOrderId_NotFound() {
        String orderId = "OR999";
        when(paymentRepository.findByOrderId(orderId)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                paymentService.getPaymentsByOrderId(orderId)
        );

        assertEquals("No payments found for order ID: OR999", exception.getMessage());
        verify(paymentRepository, times(1)).findByOrderId(orderId);
    }
}