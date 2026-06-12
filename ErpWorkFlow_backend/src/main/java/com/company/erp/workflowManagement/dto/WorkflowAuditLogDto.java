package com.company.erp.workflowManagement.dto;
import java.time.LocalDateTime;
public class WorkflowAuditLogDto {
    private String userName;
    private String action;
    private LocalDateTime actionTime;
    public WorkflowAuditLogDto() {}
    public WorkflowAuditLogDto(String userName, String action, LocalDateTime actionTime) {
        this.userName = userName; this.action = action; this.actionTime = actionTime;
    }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }
}
