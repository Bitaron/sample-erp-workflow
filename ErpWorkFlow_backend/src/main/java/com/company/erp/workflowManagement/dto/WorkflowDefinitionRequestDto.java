package com.company.erp.workflowManagement.dto;
public class WorkflowDefinitionRequestDto {
    private String name;
    private String documentType;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
}
