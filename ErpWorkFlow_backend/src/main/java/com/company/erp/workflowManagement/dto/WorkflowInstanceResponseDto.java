package com.company.erp.workflowManagement.dto;
public class WorkflowInstanceResponseDto {
    private Long workflowInstanceId;
    private String status;
    public WorkflowInstanceResponseDto() {}
    public WorkflowInstanceResponseDto(Long workflowInstanceId, String status) {
        this.workflowInstanceId = workflowInstanceId;
        this.status = status;
    }
    public Long getWorkflowInstanceId() { return workflowInstanceId; }
    public void setWorkflowInstanceId(Long workflowInstanceId) { this.workflowInstanceId = workflowInstanceId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
