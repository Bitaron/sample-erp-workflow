package com.company.erp.invoiceManagement.service;
import com.company.erp.invoiceManagement.dto.InvoiceDto;
import java.util.List;
public interface InvoiceService {
    void generateInvoice(Long billingRequestId);
    List<InvoiceDto> getAllInvoices();
    InvoiceDto getInvoiceById(Long id);
}
