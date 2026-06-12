package com.company.erp.workflowManagement.dto;
import java.util.List;
public class WorkflowDefinitionResponseDto {
    private Long id;
    private String name;
    private String documentType;
    private String createdBy;
    private String createdTime;
    private List<WorkflowStepResponseDto> steps;
    public WorkflowDefinitionResponseDto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }
    public List<WorkflowStepResponseDto> getSteps() { return steps; }
    public void setSteps(List<WorkflowStepResponseDto> steps) { this.steps = steps; }
}
