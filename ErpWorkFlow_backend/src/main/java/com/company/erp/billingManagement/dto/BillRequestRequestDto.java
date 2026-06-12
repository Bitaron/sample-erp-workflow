package com.company.erp.billingManagement.dto;
public class BillRequestRequestDto {
    private Long customerId;
    private Double amount;
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
