package com.customer_service.controller;

import com.customer_service.dto.CustomerResponse;
import com.customer_service.entity.Customer;
import com.customer_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.saveCustomer(customer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerDetails(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerDetails(customerId));
    }
}
