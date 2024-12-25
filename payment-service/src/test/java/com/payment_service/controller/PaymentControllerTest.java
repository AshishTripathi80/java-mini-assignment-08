package com.payment_service.controller;

import com.payment_service.entity.Payment;
import com.payment_service.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePayment_Success() {
        // Arrange
        Payment payment = createMockPayment(
                null,
                "customer456",
                "order123",
                100.00,
                "USD",
                "SUCCESS",
                LocalDateTime.now()
        );

        when(paymentService.savePayment(payment)).thenReturn(payment);

        // Act
        ResponseEntity<Payment> response = paymentController.savePayment(payment);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(payment, response.getBody());
        verify(paymentService, times(1)).savePayment(payment);
    }

    @Test
    void testGetPaymentsByOrderId_Success() {
        // Arrange
        String orderId = "order123";
        List<Payment> expectedPayments = Arrays.asList(
                createMockPayment(1L, "customer1", orderId, 50.00, "USD", "SUCCESS", LocalDateTime.now()),
                createMockPayment(2L, "customer2", orderId, 75.00, "USD", "SUCCESS", LocalDateTime.now())
        );

        when(paymentService.getPaymentsByOrderId(orderId)).thenReturn(expectedPayments);

        // Act
        ResponseEntity<List<Payment>> response = paymentController.getPaymentsByOrderId(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(paymentService, times(1)).getPaymentsByOrderId(orderId);
    }

    @Test
    void testGetPaymentsByCustomerId_Success() {
        // Arrange
        String customerId = "customer456";
        List<Payment> expectedPayments = Arrays.asList(
                createMockPayment(1L, customerId, "order1", 50.00, "USD", "SUCCESS", LocalDateTime.now()),
                createMockPayment(2L, customerId, "order2", 75.00, "USD", "SUCCESS", LocalDateTime.now())
        );

        when(paymentService.getPaymentsByCustomerId(customerId)).thenReturn(expectedPayments);

        // Act
        ResponseEntity<List<Payment>> response = paymentController.getPaymentsByCustomerId(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(paymentService, times(1)).getPaymentsByCustomerId(customerId);
    }

    @Test
    void testGetPaymentsByOrderId_NoPayments() {
        // Arrange
        String orderId = "nonexistent-order";
        when(paymentService.getPaymentsByOrderId(orderId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<Payment>> response = paymentController.getPaymentsByOrderId(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void testGetPaymentsByCustomerId_NoPayments() {
        // Arrange
        String customerId = "nonexistent-customer";
        when(paymentService.getPaymentsByCustomerId(customerId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<Payment>> response = paymentController.getPaymentsByCustomerId(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    // Utility method to create mock payments with more comprehensive parameters
    private Payment createMockPayment(Long id, String customerId, String orderId,
                                      Double paymentAmount, String currency,
                                      String paymentStatus, LocalDateTime paymentTime) {
        Payment payment = new Payment(customerId, orderId, paymentAmount, currency, paymentStatus, paymentTime);
        if (id != null) {
            payment.setId(id);
        }
        return payment;
    }
}