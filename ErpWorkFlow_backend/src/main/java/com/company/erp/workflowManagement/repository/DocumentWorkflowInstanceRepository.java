package com.company.erp.workflowManagement.repository;
import com.company.erp.workflowManagement.entity.DocumentWorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface DocumentWorkflowInstanceRepository extends JpaRepository<DocumentWorkflowInstance, Long> {
    Optional<DocumentWorkflowInstance> findByDocumentId(Long documentId);
}
