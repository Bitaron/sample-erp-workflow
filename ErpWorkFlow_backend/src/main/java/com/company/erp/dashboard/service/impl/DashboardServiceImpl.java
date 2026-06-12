package com.company.erp.dashboard.service.impl;

import com.company.erp.dashboard.dto.DashboardSummaryDto;
import com.company.erp.dashboard.service.DashboardService;
import com.company.erp.userManagement.repository.UserRepository;
import com.company.erp.workflowManagement.repository.DocumentWorkflowAuditLogRepository;
import com.company.erp.workflowManagement.repository.DocumentWorkflowInstanceRepository;
import com.company.erp.userManagement.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final DocumentWorkflowInstanceRepository instanceRepo;
    private final DocumentWorkflowAuditLogRepository auditRepo;
    private final UserRepository userRepo;

    public DashboardServiceImpl(DocumentWorkflowInstanceRepository instanceRepo, DocumentWorkflowAuditLogRepository auditRepo, UserRepository userRepo) {
        this.instanceRepo = instanceRepo;
        this.auditRepo = auditRepo;
        this.userRepo = userRepo;
    }

    @Override
    public DashboardSummaryDto getSummary() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        long totalInProgress = instanceRepo.countByStatus("IN_PROGRESS");
        long totalCompleted = instanceRepo.countByStatus("COMPLETED");

        Long myPending = 0L;
        Long myCompleted = 0L;

        try {
            User user = userRepo.findByUserName(username).orElse(null);
            if (user != null && user.getDepartment() != null && user.getDepartment().getId() != null) {
                Long deptId = user.getDepartment().getId();
                myPending = instanceRepo.countByStatusAndCurrentWorkflowStepDepartmentId("IN_PROGRESS", deptId);
            }
            myCompleted = auditRepo.countByActionTakenByAndAction(username, "APPROVED");
        } catch (Exception ex) {
            // If anything goes wrong (e.g. unauthenticated), return zeros for personal fields
            myPending = 0L;
            myCompleted = 0L;
        }

        return new DashboardSummaryDto(totalInProgress, totalCompleted, myPending, myCompleted);
    }
}

