package com.customer_service.controller;

import com.customer_service.dto.CustomerResponse;
import com.customer_service.dto.OrderDto;
import com.customer_service.dto.PaymentDto;
import com.customer_service.entity.Customer;
import com.customer_service.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CustomerService customerService() {
            return Mockito.mock(CustomerService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        // Reset mock between tests
        Mockito.reset(customerService);

        // Setup test data
        customer = new Customer("John Doe", "john@example.com");
        customer.setId(1L);

        // Create sample order and payment data
        OrderDto order = new OrderDto(1L, "Product1", "Category1", "2023-01-01", "2023-01-10");
        PaymentDto payment = new PaymentDto(1L, "USD", 100.0, "COMPLETED");

        customerResponse = new CustomerResponse(
                customer,
                Collections.singletonList(order),
                Collections.singletonList(Collections.singletonList(payment))
        );
    }

    @Test
    void testSaveCustomer_Success() throws Exception {
        // Arrange
        when(customerService.saveCustomer(any(Customer.class)))
                .thenReturn(customer);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        // Verify
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testGetCustomerDetails_Success() throws Exception {
        // Arrange
        when(customerService.getCustomerDetails(anyLong()))
                .thenReturn(customerResponse);

        // Act & Assert
        mockMvc.perform(get("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(1L))
                .andExpect(jsonPath("$.customer.customerName").value("John Doe"))
                .andExpect(jsonPath("$.orders[0].id").value(1L))
                .andExpect(jsonPath("$.payments[0][0].paymentStatus").value("COMPLETED"));

        // Verify
        verify(customerService).getCustomerDetails(1L);
    }
}