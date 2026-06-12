package com.company.erp.workflowManagement.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class DocumentWorkflowInstance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long documentId;
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private WorkflowDefinition workflowDefinition;
    private String status;
    private String initiatedBy;
    private LocalDateTime initiationTime;
    @ManyToOne
    @JoinColumn(name = "current_workflow_step_id")
    private WorkflowStep currentWorkflowStep;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    public WorkflowDefinition getWorkflowDefinition() { return workflowDefinition; }
    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) { this.workflowDefinition = workflowDefinition; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
    public LocalDateTime getInitiationTime() { return initiationTime; }
    public void setInitiationTime(LocalDateTime initiationTime) { this.initiationTime = initiationTime; }
    public WorkflowStep getCurrentWorkflowStep() { return currentWorkflowStep; }
    public void setCurrentWorkflowStep(WorkflowStep currentWorkflowStep) { this.currentWorkflowStep = currentWorkflowStep; }
}
