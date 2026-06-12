package com.company.erp.invoiceManagement.dto;
import java.time.LocalDateTime;
public class InvoiceDto {
    private Long id;
    private Long billingRequestId;
    private String invoiceNo;
    private LocalDateTime createdAt;
    public InvoiceDto() {}
    public InvoiceDto(Long id, Long billingRequestId, String invoiceNo, LocalDateTime createdAt) {
        this.id = id; this.billingRequestId = billingRequestId; this.invoiceNo = invoiceNo; this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBillingRequestId() { return billingRequestId; }
    public void setBillingRequestId(Long billingRequestId) { this.billingRequestId = billingRequestId; }
    public String getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
