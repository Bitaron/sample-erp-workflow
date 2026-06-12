package com.company.erp.workflowManagement.service.impl;
import com.company.erp.invoiceManagement.service.InvoiceService;
import com.company.erp.userManagement.entity.User;
import com.company.erp.userManagement.repository.UserRepository;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.entity.*;
import com.company.erp.workflowManagement.repository.*;
import com.company.erp.workflowManagement.service.WorkflowInstanceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
@Service
public class WorkflowInstanceServiceImpl implements WorkflowInstanceService {
    private final DocumentWorkflowInstanceRepository instanceRepo;
    private final DocumentWorkflowAuditLogRepository auditRepo;
    private final WorkflowDefinitionRepository defRepo;
    private final UserRepository userRepo;
    private final InvoiceService invoiceService;
    public WorkflowInstanceServiceImpl(DocumentWorkflowInstanceRepository instanceRepo, DocumentWorkflowAuditLogRepository auditRepo, WorkflowDefinitionRepository defRepo, UserRepository userRepo, InvoiceService invoiceService) {
        this.instanceRepo = instanceRepo; this.auditRepo = auditRepo; this.defRepo = defRepo; this.userRepo = userRepo; this.invoiceService = invoiceService;
    }
    @Override
    public WorkflowInstanceResponseDto startWorkflow(StartWorkflowRequestDto request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        WorkflowDefinition def = defRepo.findAll().stream().filter(d -> "BILL_REQUEST".equals(d.getDocumentType())).findFirst().orElseThrow(() -> new RuntimeException("Workflow def not found"));
        WorkflowStep firstStep = def.getSteps().stream().min(Comparator.comparing(WorkflowStep::getSequenceOrder)).orElseThrow(() -> new RuntimeException("No steps"));
        DocumentWorkflowInstance instance = new DocumentWorkflowInstance();
        instance.setDocumentId(request.getDocumentId());
        instance.setWorkflowDefinition(def);
        instance.setStatus("IN_PROGRESS");
        instance.setInitiatedBy(username);
        instance.setInitiationTime(LocalDateTime.now());
        instance.setCurrentWorkflowStep(firstStep);
        instance = instanceRepo.save(instance);
        return new WorkflowInstanceResponseDto(instance.getId(), instance.getStatus());
    }
    @Override
    public WorkflowInstanceResponseDto approveWorkflow(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUserName(username).orElseThrow();
        DocumentWorkflowInstance instance = instanceRepo.findById(id).orElseThrow();
        if ("COMPLETED".equals(instance.getStatus())) throw new RuntimeException("Already completed");
        if (!user.getDepartment().getId().equals(instance.getCurrentWorkflowStep().getDepartment().getId())) {
            throw new com.company.erp.common.exception.UnauthorizedException("User department does not match step department");
        }
        DocumentWorkflowAuditLog audit = new DocumentWorkflowAuditLog();
        audit.setWorkflowInstance(instance);
        audit.setWorkflowStep(instance.getCurrentWorkflowStep());
        audit.setAction("APPROVED");
        audit.setActionTakenBy(username);
        audit.setActionTakenTime(LocalDateTime.now());
        auditRepo.save(audit);
        
        List<WorkflowStep> steps = instance.getWorkflowDefinition().getSteps();
        WorkflowStep current = instance.getCurrentWorkflowStep();
        WorkflowStep next = steps.stream().filter(s -> s.getSequenceOrder() > current.getSequenceOrder()).min(Comparator.comparing(WorkflowStep::getSequenceOrder)).orElse(null);
        if (next == null) {
            instance.setStatus("COMPLETED");
            instance.setCurrentWorkflowStep(null);
            invoiceService.generateInvoice(instance.getDocumentId());
        } else {
            instance.setCurrentWorkflowStep(next);
        }
        instance = instanceRepo.save(instance);
        return new WorkflowInstanceResponseDto(instance.getId(), instance.getStatus());
    }
    @Override
    public WorkflowInstanceResponseDto getWorkflowInstance(Long id) {
        DocumentWorkflowInstance instance = instanceRepo.findById(id).orElseThrow();
        return new WorkflowInstanceResponseDto(instance.getId(), instance.getStatus());
    }
    @Override
    public List<WorkflowAuditLogDto> getAuditLogs(Long id) {
        return auditRepo.findByWorkflowInstanceId(id).stream().map(a -> new WorkflowAuditLogDto(a.getActionTakenBy(), a.getAction(), a.getActionTakenTime())).collect(Collectors.toList());
    }
}
