package com.company.erp.billingManagement.controller;
import com.company.erp.billingManagement.dto.*;
import com.company.erp.billingManagement.service.BillRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-requests")
public class BillRequestController {
    private final BillRequestService billRequestService;
    public BillRequestController(BillRequestService billRequestService) { this.billRequestService = billRequestService; }
    @PostMapping
    public BillRequestResponseDto createBillRequest(@RequestBody BillRequestRequestDto request) {
        return billRequestService.createBillRequest(request);
    }
    @GetMapping
    public List<BillRequestResponseDto> getBillRequests(Pageable pageable) {
        return billRequestService.getBillRequests(pageable);
    }
    @GetMapping("/{id}")
    public BillRequestResponseDto getBillRequestById(@PathVariable Long id) {
        return billRequestService.getBillRequestById(id);
    }
}
