package com.customer_service.dto;

public class PaymentDto {
    private Long id;
    private String currency;
    private Double paymentAmount;
    private String paymentStatus;

    public PaymentDto() {
    }

    public PaymentDto(Long id, String currency, Double paymentAmount, String paymentStatus) {
        this.id = id;
        this.currency = currency;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
