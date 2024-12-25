package com.customer_service.dto;

import com.customer_service.entity.Customer;

import java.util.List;

public class CustomerResponse {
    private Customer customer;
    private List<OrderDto> orders;
    private List<List<PaymentDto>> payments;

    public CustomerResponse(Customer customer, List<OrderDto> orders, List<List<PaymentDto>> payments) {
        this.customer = customer;
        this.orders = orders;
        this.payments = payments;
    }

    public CustomerResponse() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }

    public List<List<PaymentDto>> getPayments() {
        return payments;
    }

    public void setPayments(List<List<PaymentDto>> payments) {
        this.payments = payments;
    }
}
