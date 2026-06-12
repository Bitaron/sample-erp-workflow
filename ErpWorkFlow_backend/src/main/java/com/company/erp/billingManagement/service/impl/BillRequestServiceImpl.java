package com.company.erp.billingManagement.service.impl;
import com.company.erp.billingManagement.dto.*;
import com.company.erp.billingManagement.entity.BillRequest;
import com.company.erp.billingManagement.repository.BillRequestRepository;
import com.company.erp.billingManagement.service.BillRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
@Service
public class BillRequestServiceImpl implements BillRequestService {
    private final BillRequestRepository repository;
    public BillRequestServiceImpl(BillRequestRepository repository) { this.repository = repository; }
    @Override
    public BillRequestResponseDto createBillRequest(BillRequestRequestDto request) {
        BillRequest br = new BillRequest();
        br.setCustomerId(request.getCustomerId());
        br.setAmount(request.getAmount());
        br.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        br.setCreatedTime(LocalDateTime.now());
        br.setStatus("IN_PROGRESS");
        br = repository.save(br);
        return mapToDto(br);
    }
    @Override
    public Page<BillRequestResponseDto> getBillRequests(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToDto);
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
        dto.setInvoiceNumber(br.getInvoiceNumber());
        dto.setCanApprove(false); // To be implemented in Phase 4
        return dto;
    }
}
