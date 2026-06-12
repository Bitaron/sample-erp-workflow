package com.company.erp.workflowManagement.controller;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.service.WorkflowInstanceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/workflow-instances")
public class WorkflowInstanceController {
    private final WorkflowInstanceService service;
    public WorkflowInstanceController(WorkflowInstanceService service) { this.service = service; }
    @PostMapping("/start")
    public WorkflowInstanceResponseDto startWorkflow(@RequestBody StartWorkflowRequestDto request) { return service.startWorkflow(request); }
    @PostMapping("/{id}/approve")
    public WorkflowInstanceResponseDto approveWorkflow(@PathVariable Long id) { return service.approveWorkflow(id); }
    @GetMapping("/{id}")
    public WorkflowInstanceResponseDto getWorkflowInstance(@PathVariable Long id) { return service.getWorkflowInstance(id); }
    @GetMapping("/{id}/audit")
    public List<WorkflowAuditLogDto> getAuditLogs(@PathVariable Long id) { return service.getAuditLogs(id); }
}
