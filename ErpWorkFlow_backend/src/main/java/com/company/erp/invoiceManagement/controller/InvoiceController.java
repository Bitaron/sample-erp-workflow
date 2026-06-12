package com.company.erp.invoiceManagement.controller;
import com.company.erp.invoiceManagement.dto.InvoiceDto;
import com.company.erp.invoiceManagement.service.InvoiceService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService) { this.invoiceService = invoiceService; }
    @GetMapping
    public List<InvoiceDto> getInvoices() { return invoiceService.getAllInvoices(); }
    @GetMapping("/{id}")
    public InvoiceDto getInvoiceById(@PathVariable Long id) { return invoiceService.getInvoiceById(id); }
}
