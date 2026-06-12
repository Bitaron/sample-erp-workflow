package com.company.erp.billingManagement.dto;
import java.time.LocalDateTime;
public class BillRequestResponseDto {
    private Long id;
    private Long customerId;
    private Double amount;
    private String createdBy;
    private LocalDateTime createdTime;
    private String status;
    private String invoiceNumber;
    private Long invoiceId;
    private Boolean canApprove;
    private Long workflowInstanceId;
    public BillRequestResponseDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public Boolean getCanApprove() { return canApprove; }
    public void setCanApprove(Boolean canApprove) { this.canApprove = canApprove; }

    public Long getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    public void setWorkflowInstanceId(Long workflowInstanceId) {
        this.workflowInstanceId = workflowInstanceId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
}
