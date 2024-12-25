package com.customer_service.service;

import com.customer_service.dto.CustomerResponse;
import com.customer_service.dto.OrderDto;
import com.customer_service.dto.PaymentDto;
import com.customer_service.entity.Customer;
import com.customer_service.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WebClient orderWebClient;

    @Mock
    private WebClient paymentWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(
                customerRepository,
                orderWebClient,
                paymentWebClient
        );
    }

    @Test
    void testGetCustomerDetails_Success() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer("John Doe", "john@example.com");
        customer.setId(customerId);

        // Mock customer repository
        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        // Prepare test data
        OrderDto order1 = new OrderDto(1L, "Product1", "Category1", "2023-01-01", "2023-01-10");
        OrderDto order2 = new OrderDto(2L, "Product2", "Category2", "2023-02-01", "2023-02-10");
        List<OrderDto> orders = Arrays.asList(order1, order2);

        PaymentDto payment1 = new PaymentDto(1L, "USD", 100.0, "COMPLETED");
        PaymentDto payment2 = new PaymentDto(2L, "USD", 200.0, "PENDING");

        // Mock order WebClient
        when(orderWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(OrderDto.class))
                .thenReturn(Flux.fromIterable(orders));

        // Mock payment WebClient for each order
        when(paymentWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(PaymentDto.class))
                .thenReturn(Flux.fromIterable(List.of(payment1)))
                .thenReturn(Flux.fromIterable(List.of(payment2)));

        // Act
        CustomerResponse customerResponse = customerService.getCustomerDetails(customerId);

        // Assert
        assertNotNull(customerResponse);
        assertEquals(customer, customerResponse.getCustomer());
        assertEquals(2, customerResponse.getOrders().size());
        assertEquals(2, customerResponse.getPayments().size());

        verify(customerRepository).findById(customerId);
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        Customer customer = new Customer("John Doe", "john@example.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Assert
        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getCustomerName());
        verify(customerRepository).save(customer);
    }

    @Test
    void testGetCustomerDetails_CustomerNotFound() {
        // Arrange
        Long customerId = 1L;
        when(customerRepository.findById(customerId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerDetails(customerId);
        });
    }
}