package com.customer_service.dto;


public class OrderDto {
    private Long id;
    private String productName;
    private String productCategory;
    private String orderDate;
    private String deliveryDate;

    public OrderDto() {
    }

    public OrderDto(Long id, String productName, String productCategory, String orderDate, String deliveryDate) {
        this.id = id;
        this.productName = productName;
        this.productCategory = productCategory;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    // Getters and Setters
}
