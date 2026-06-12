package com.company.erp.workflowManagement.service.impl;
import com.company.erp.userManagement.entity.Department;
import com.company.erp.userManagement.repository.DepartmentRepository;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.entity.*;
import com.company.erp.workflowManagement.repository.*;
import com.company.erp.workflowManagement.service.WorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class WorkflowServiceImpl implements WorkflowService {
    private final WorkflowDefinitionRepository workflowDefRepo;
    private final WorkflowStepRepository workflowStepRepo;
    private final DepartmentRepository departmentRepo;
    public WorkflowServiceImpl(WorkflowDefinitionRepository wdr, WorkflowStepRepository wsr, DepartmentRepository dr) {
        this.workflowDefRepo = wdr; this.workflowStepRepo = wsr; this.departmentRepo = dr;
    }
    @Override
    public WorkflowDefinitionResponseDto createWorkflow(WorkflowDefinitionRequestDto request) {
        WorkflowDefinition wd = new WorkflowDefinition();
        wd.setName(request.getName());
        wd.setDocumentType(request.getDocumentType());
        wd.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        wd.setCreatedTime(LocalDateTime.now());
        wd = workflowDefRepo.save(wd);
        WorkflowDefinitionResponseDto res = new WorkflowDefinitionResponseDto();
        res.setId(wd.getId());
        res.setName(wd.getName());
        res.setDocumentType(wd.getDocumentType());
        res.setCreatedBy(wd.getCreatedBy());
        res.setCreatedTime(wd.getCreatedTime() != null ? wd.getCreatedTime().toString() : null);
        return res;
    }
    @Override
    public WorkflowStepResponseDto addWorkflowStep(Long workflowId, WorkflowStepRequestDto request) {
        WorkflowDefinition wd = workflowDefRepo.findById(workflowId).orElseThrow(() -> new RuntimeException("Workflow not found"));
        Department dept = departmentRepo.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));
        WorkflowStep step = new WorkflowStep();
        step.setWorkflowDefinition(wd);
        step.setDepartment(dept);
        step.setSequenceOrder(request.getSequenceOrder());
        step = workflowStepRepo.save(step);
        return new WorkflowStepResponseDto(step.getId(), step.getSequenceOrder(), dept.getName());
    }
    @Override
    public List<WorkflowDefinitionResponseDto> getAllWorkflows() {
        return workflowDefRepo.findAll().stream().map(wd -> {
            WorkflowDefinitionResponseDto res = new WorkflowDefinitionResponseDto();
            res.setId(wd.getId()); res.setName(wd.getName()); res.setDocumentType(wd.getDocumentType());
            res.setCreatedBy(wd.getCreatedBy()); res.setCreatedTime(wd.getCreatedTime() != null ? wd.getCreatedTime().toString() : null);
            return res;
        }).collect(Collectors.toList());
    }
    @Override
    public WorkflowDefinitionResponseDto getWorkflowById(Long id) {
        WorkflowDefinition wd = workflowDefRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        WorkflowDefinitionResponseDto res = new WorkflowDefinitionResponseDto();
        res.setId(wd.getId()); res.setName(wd.getName()); res.setDocumentType(wd.getDocumentType());
        res.setCreatedBy(wd.getCreatedBy()); res.setCreatedTime(wd.getCreatedTime() != null ? wd.getCreatedTime().toString() : null);
        if (wd.getSteps() != null) {
            res.setSteps(wd.getSteps().stream().map(s -> new WorkflowStepResponseDto(s.getId(), s.getSequenceOrder(), s.getDepartment() != null ? s.getDepartment().getName() : null)).collect(Collectors.toList()));
        }
        return res;
    }
}
