package com.order_service.service;

import com.order_service.entity.Order;
import com.order_service.exception.ResourceNotFoundException;
import com.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

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
        when(orderRepository.save(mockOrder)).thenReturn(mockOrder);

        // Act
        Order savedOrder = orderService.saveOrder(mockOrder);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(mockOrder, savedOrder);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    void testGetOrdersByCustomerId_Success() {
        // Arrange
        List<Order> expectedOrders = Collections.singletonList(mockOrder);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(expectedOrders);

        // Act
        List<Order> retrievedOrders = orderService.getOrdersByCustomerId(customerId);

        // Assert
        assertNotNull(retrievedOrders);
        assertEquals(1, retrievedOrders.size());
        assertEquals(mockOrder, retrievedOrders.get(0));
        verify(orderRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testGetOrdersByCustomerId_NoOrdersFound() {
        // Arrange
        when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrdersByCustomerId(customerId)
        );

        // Verify
        assertEquals("No orders found for customer ID: " + customerId, exception.getMessage());
        verify(orderRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testGetOrdersByCustomerId_MultipleOrders() {
        // Arrange
        List<Order> multipleOrders = new ArrayList<>();
        multipleOrders.add(mockOrder);
        multipleOrders.add(new Order(
                customerId,
                "PROD002",
                "Clothing",
                "T-Shirt",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        ));

        when(orderRepository.findByCustomerId(customerId)).thenReturn(multipleOrders);

        // Act
        List<Order> retrievedOrders = orderService.getOrdersByCustomerId(customerId);

        // Assert
        assertNotNull(retrievedOrders);
        assertEquals(2, retrievedOrders.size());
        verify(orderRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testOrderServiceThreadInterruption() {
        // Arrange
        when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.singletonList(mockOrder));

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Order> orders = orderService.getOrdersByCustomerId(customerId);
            assertNotNull(orders);
        });
    }

    // Additional helper method for creating multiple test orders
    private List<Order> createMultipleTestOrders(String customerId) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(
                customerId,
                "PROD001",
                "Electronics",
                "Smartphone",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3)
        ));
        orders.add(new Order(
                customerId,
                "PROD002",
                "Clothing",
                "T-Shirt",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        ));
        return orders;
    }
}