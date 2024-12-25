package com.order_service.service;



import com.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order); // Save a new order
    List<Order> getOrdersByCustomerId(String customerId); // Fetch orders for a customer
}
