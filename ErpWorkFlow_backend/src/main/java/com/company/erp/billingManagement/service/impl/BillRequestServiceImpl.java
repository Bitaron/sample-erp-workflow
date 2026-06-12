package com.company.erp.billingManagement.service.impl;

import com.company.erp.billingManagement.dto.BillRequestRequestDto;
import com.company.erp.billingManagement.dto.BillRequestResponseDto;
import com.company.erp.billingManagement.entity.BillRequest;
import com.company.erp.billingManagement.repository.BillRequestRepository;
import com.company.erp.billingManagement.service.BillRequestService;
import com.company.erp.invoiceManagement.entity.Invoice;
import com.company.erp.invoiceManagement.repository.InvoiceRepository;
import com.company.erp.userManagement.entity.User;
import com.company.erp.userManagement.repository.UserRepository;
import com.company.erp.workflowManagement.dto.StartWorkflowRequestDto;
import com.company.erp.workflowManagement.entity.DocumentWorkflowInstance;
import com.company.erp.workflowManagement.repository.DocumentWorkflowInstanceRepository;
import com.company.erp.workflowManagement.service.WorkflowInstanceService;
import com.company.erp.workflowManagement.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillRequestServiceImpl implements BillRequestService {
    @Autowired
    private BillRequestRepository repository;
    @Autowired
    private DocumentWorkflowInstanceRepository workflowInstanceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkflowInstanceService workflowInstanceService;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public BillRequestResponseDto createBillRequest(BillRequestRequestDto request) {
        BillRequest br = new BillRequest();
        br.setCustomerId(request.getCustomerId());
        br.setAmount(request.getAmount());
        br.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        br.setCreatedTime(LocalDateTime.now());
        br.setStatus("IN_PROGRESS");
        br = repository.save(br);
        StartWorkflowRequestDto req = new StartWorkflowRequestDto();
        req.setDocumentId(br.getId());
        workflowInstanceService.startWorkflow(req);
        return mapToDto(br);
    }

    @Override
    public List<BillRequestResponseDto> getBillRequests(Pageable pageable) {
        return repository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public BillRequestResponseDto getBillRequestById(Long id) {
        return repository.findById(id).map(this::mapToDto).orElseThrow(() -> new RuntimeException("Not found"));
    }

    private BillRequestResponseDto mapToDto(BillRequest br) {
        BillRequestResponseDto dto = new BillRequestResponseDto();
        dto.setId(br.getId());
        dto.setCustomerId(br.getCustomerId());
        dto.setAmount(br.getAmount());
        dto.setCreatedBy(br.getCreatedBy());
        dto.setCreatedTime(br.getCreatedTime());
        dto.setStatus(br.getStatus());
        Optional<Invoice> optionalInvoice = invoiceRepository.findByBillingRequestId(br.getId());
        if (optionalInvoice.isPresent()) {
            dto.setInvoiceNumber(optionalInvoice.get().getInvoiceNo());
            dto.setInvoiceId(optionalInvoice.get().getId());
        }
        Optional<DocumentWorkflowInstance> workflowInstance =
                workflowInstanceRepository.findByDocumentId(br.getId());
        Optional<User> user = userRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (workflowInstance.isPresent() && user.isPresent()
                && user.get().getDepartment() != null) {
            if (!"COMPLETED".equals(workflowInstance.get().getStatus())) {
                dto.setCanApprove(workflowInstance.get().getCurrentWorkflowStep().getDepartment().getId()
                        .equals(user.get().getDepartment().getId()));
            }
            dto.setWorkflowInstanceId(workflowInstance.get().getId());
        } else {
            dto.setCanApprove(false);
        }
        return dto;
    }
}
