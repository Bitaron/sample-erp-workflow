package com.company.erp.billingManagement.service;
import com.company.erp.billingManagement.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillRequestService {
    BillRequestResponseDto createBillRequest(BillRequestRequestDto request);
    List<BillRequestResponseDto> getBillRequests(Pageable pageable);
    BillRequestResponseDto getBillRequestById(Long id);
}
