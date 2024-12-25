package com.customer_service.service;


import com.customer_service.dto.CustomerResponse;
import com.customer_service.entity.Customer;

public interface CustomerService {
    Customer saveCustomer(Customer customer); // Save customer details
    CustomerResponse getCustomerDetails(Long customerId);
}

