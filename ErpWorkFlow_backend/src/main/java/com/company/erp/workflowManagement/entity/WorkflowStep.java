package com.company.erp.workflowManagement.entity;
import com.company.erp.userManagement.entity.Department;
import jakarta.persistence.*;
@Entity
public class WorkflowStep {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer sequenceOrder;
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private WorkflowDefinition workflowDefinition;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
    public WorkflowDefinition getWorkflowDefinition() { return workflowDefinition; }
    public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) { this.workflowDefinition = workflowDefinition; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}
