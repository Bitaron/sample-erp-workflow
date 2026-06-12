package com.company.erp.workflowManagement.service;
import com.company.erp.workflowManagement.dto.*;
import java.util.List;
public interface WorkflowService {
    WorkflowDefinitionResponseDto createWorkflow(WorkflowDefinitionRequestDto request);
    WorkflowStepResponseDto addWorkflowStep(Long workflowId, WorkflowStepRequestDto request);
    List<WorkflowDefinitionResponseDto> getAllWorkflows();
    WorkflowDefinitionResponseDto getWorkflowById(Long id);
}
