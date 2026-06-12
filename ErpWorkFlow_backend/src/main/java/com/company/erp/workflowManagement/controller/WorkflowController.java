package com.company.erp.workflowManagement.controller;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.service.WorkflowService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {
    private final WorkflowService workflowService;
    public WorkflowController(WorkflowService workflowService) { this.workflowService = workflowService; }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public WorkflowDefinitionResponseDto createWorkflow(@RequestBody WorkflowDefinitionRequestDto request) { return workflowService.createWorkflow(request); }
    @PostMapping("/{workflowId}/steps")
    @PreAuthorize("hasRole('ADMIN')")
    public WorkflowStepResponseDto addWorkflowStep(@PathVariable Long workflowId, @RequestBody WorkflowStepRequestDto request) { return workflowService.addWorkflowStep(workflowId, request); }
    @GetMapping
    public List<WorkflowDefinitionResponseDto> getAllWorkflows() { return workflowService.getAllWorkflows(); }
    @GetMapping("/{id}")
    public WorkflowDefinitionResponseDto getWorkflowById(@PathVariable Long id) { return workflowService.getWorkflowById(id); }
}
