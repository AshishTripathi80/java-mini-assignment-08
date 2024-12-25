package com.customer_service.service;

import com.customer_service.dto.CustomerResponse;
import com.customer_service.dto.OrderDto;
import com.customer_service.dto.PaymentDto;
import com.customer_service.entity.Customer;
import com.customer_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final WebClient orderWebClient;
    private final WebClient paymentWebClient;

    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            @Qualifier("orderServiceWebClient") WebClient orderWebClient,
            @Qualifier("paymentServiceWebClient") WebClient paymentWebClient
    ) {
        this.customerRepository = customerRepository;
        this.orderWebClient = orderWebClient;
        this.paymentWebClient = paymentWebClient;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public CustomerResponse getCustomerDetails(Long customerId) {
        // Fetch customer from database
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Fetch orders for the customer
        List<OrderDto> orders = orderWebClient.get()
                .uri("/customer/" + customerId)
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .collectList()
                .block();

        // Fetch payments for each order
        List<List<PaymentDto>> payments = orders.stream()
                .map(order -> paymentWebClient.get()
                        .uri("/order/" + order.getId())
                        .retrieve()
                        .bodyToFlux(PaymentDto.class)
                        .collectList()
                        .block())
                .toList();

        // Aggregate data and return response
        return new CustomerResponse(customer, orders, payments);
    }
}
