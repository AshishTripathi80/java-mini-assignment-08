package com.order_service.repository;


import com.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query method to find orders by customer ID
    List<Order> findByCustomerId(String customerId);
}
