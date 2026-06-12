package com.company.erp.workflowManagement.dto;
public class WorkflowStepResponseDto {
    private Long id;
    private Integer sequence;
    private String departmentName;
    public WorkflowStepResponseDto() {}
    public WorkflowStepResponseDto(Long id, Integer sequence, String departmentName) {
        this.id = id; this.sequence = sequence; this.departmentName = departmentName;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
