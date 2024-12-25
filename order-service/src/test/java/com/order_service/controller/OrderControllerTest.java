package com.order_service.controller;

import com.order_service.entity.Order;
import com.order_service.exception.ResourceNotFoundException;
import com.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order mockOrder;
    private String customerId;

    @BeforeEach
    void setUp() {
        customerId = "CU123";
        mockOrder = new Order(
                customerId,
                "PROD001",
                "Electronics",
                "Smartphone",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        );
    }

    @Test
    void testSaveOrder_Success() {
        // Arrange
        when(orderService.saveOrder(mockOrder)).thenReturn(mockOrder);

        // Act
        ResponseEntity<Order> response = orderController.saveOrder(mockOrder);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).saveOrder(mockOrder);
    }



    @Test
    void testGetOrdersByCustomerId_Success() {
        // Arrange
        List<Order> expectedOrders = Collections.singletonList(mockOrder);
        when(orderService.getOrdersByCustomerId(customerId)).thenReturn(expectedOrders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(mockOrder, response.getBody().get(0));
        verify(orderService, times(1)).getOrdersByCustomerId(customerId);
    }

    @Test
    void testGetOrdersByCustomerId_MultipleOrders() {
        // Arrange
        List<Order> multipleOrders = Arrays.asList(
                mockOrder,
                new Order(
                        customerId,
                        "PROD002",
                        "Clothing",
                        "T-Shirt",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2)
                )
        );
        when(orderService.getOrdersByCustomerId(customerId)).thenReturn(multipleOrders);

        // Act
        ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(customerId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getOrdersByCustomerId(customerId);
    }

    @Test
    void testGetOrdersByCustomerId_NoOrdersFound() {
        // Arrange
        when(orderService.getOrdersByCustomerId(customerId))
                .thenThrow(new ResourceNotFoundException("No orders found for customer ID: " + customerId));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderController.getOrdersByCustomerId(customerId)
        );

        // Verify
        assertEquals("No orders found for customer ID: " + customerId, exception.getMessage());
        verify(orderService, times(1)).getOrdersByCustomerId(customerId);
    }


    @Test
    void testSaveOrder_CompleteOrderDetails() {
        // Arrange
        Order completeOrder = new Order(
                "CU456",
                "PROD003",
                "Home Appliances",
                "Refrigerator",
                LocalDateTime.now(),
                LocalDateTime.now().plusWeeks(1)
        );
        when(orderService.saveOrder(completeOrder)).thenReturn(completeOrder);

        // Act
        ResponseEntity<Order> response = orderController.saveOrder(completeOrder);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(completeOrder, response.getBody());

        // Verify order details
        Order savedOrder = response.getBody();
        assertEquals("CU456", savedOrder.getCustomerId());
        assertEquals("PROD003", savedOrder.getProductId());
        assertEquals("Home Appliances", savedOrder.getProductCategory());
        assertEquals("Refrigerator", savedOrder.getProductName());
        assertNotNull(savedOrder.getOrderDate());
        assertNotNull(savedOrder.getDeliveryDate());
    }

    // Helper method to create multiple test orders
    private List<Order> createMultipleTestOrders(String customerId) {
        return Arrays.asList(
                new Order(
                        customerId,
                        "PROD001",
                        "Electronics",
                        "Smartphone",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(3)
                ),
                new Order(
                        customerId,
                        "PROD002",
                        "Clothing",
                        "T-Shirt",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2)
                )
        );
    }
}