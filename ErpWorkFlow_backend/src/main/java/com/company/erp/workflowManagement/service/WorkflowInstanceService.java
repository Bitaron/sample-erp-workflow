package com.company.erp.workflowManagement.service;
import com.company.erp.workflowManagement.dto.*;
import java.util.List;
public interface WorkflowInstanceService {
    WorkflowInstanceResponseDto startWorkflow(StartWorkflowRequestDto request);
    WorkflowInstanceResponseDto approveWorkflow(Long id);
    WorkflowInstanceResponseDto getWorkflowInstance(Long id);
    List<WorkflowAuditLogDto> getAuditLogs(Long id);
}
