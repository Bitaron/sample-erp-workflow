package com.company.erp.billingManagement.service;
import com.company.erp.billingManagement.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface BillRequestService {
    BillRequestResponseDto createBillRequest(BillRequestRequestDto request);
    Page<BillRequestResponseDto> getBillRequests(Pageable pageable);
    BillRequestResponseDto getBillRequestById(Long id);
}
