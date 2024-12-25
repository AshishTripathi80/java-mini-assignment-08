package com.customer_service.repository;

import com.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom method to find a customer by email
    Customer findByCustomerEmail(String email);
}
