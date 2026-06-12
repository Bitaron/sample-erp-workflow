package com.company.erp.workflowManagement.dto;
public class WorkflowStepRequestDto {
    private Long departmentId;
    private Integer sequenceOrder;
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
}
