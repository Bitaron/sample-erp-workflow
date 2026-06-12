package com.company.erp.invoiceManagement.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long billingRequestId;
    private String invoiceNo;
    private LocalDateTime createdAt;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBillingRequestId() { return billingRequestId; }
    public void setBillingRequestId(Long billingRequestId) { this.billingRequestId = billingRequestId; }
    public String getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
