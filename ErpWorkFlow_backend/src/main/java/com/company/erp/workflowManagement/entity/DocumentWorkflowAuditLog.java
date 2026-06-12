package com.company.erp.workflowManagement.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class DocumentWorkflowAuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "workflow_instance_id")
    private DocumentWorkflowInstance workflowInstance;
    @ManyToOne
    @JoinColumn(name = "workflow_step_id")
    private WorkflowStep workflowStep;
    private String action;
    private String actionTakenBy;
    private LocalDateTime actionTakenTime;
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DocumentWorkflowInstance getWorkflowInstance() { return workflowInstance; }
    public void setWorkflowInstance(DocumentWorkflowInstance workflowInstance) { this.workflowInstance = workflowInstance; }
    public WorkflowStep getWorkflowStep() { return workflowStep; }
    public void setWorkflowStep(WorkflowStep workflowStep) { this.workflowStep = workflowStep; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getActionTakenBy() { return actionTakenBy; }
    public void setActionTakenBy(String actionTakenBy) { this.actionTakenBy = actionTakenBy; }
    public LocalDateTime getActionTakenTime() { return actionTakenTime; }
    public void setActionTakenTime(LocalDateTime actionTakenTime) { this.actionTakenTime = actionTakenTime; }
}
