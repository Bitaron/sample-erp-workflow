package com.company.erp.workflowManagement.repository;
import com.company.erp.workflowManagement.entity.DocumentWorkflowAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DocumentWorkflowAuditLogRepository extends JpaRepository<DocumentWorkflowAuditLog, Long> {
    List<DocumentWorkflowAuditLog> findByWorkflowInstanceId(Long instanceId);
    long countByActionTakenByAndAction(String actionTakenBy, String action);
}
