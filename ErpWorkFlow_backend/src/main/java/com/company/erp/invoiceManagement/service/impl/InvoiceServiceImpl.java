package com.company.erp.invoiceManagement.service.impl;
import com.company.erp.invoiceManagement.dto.InvoiceDto;
import com.company.erp.invoiceManagement.entity.Invoice;
import com.company.erp.invoiceManagement.repository.InvoiceRepository;
import com.company.erp.invoiceManagement.service.InvoiceService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) { this.invoiceRepository = invoiceRepository; }
    @Override
    public void generateInvoice(Long billingRequestId) {
        Invoice invoice = new Invoice();
        invoice.setBillingRequestId(billingRequestId);
        invoice.setInvoiceNo("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setCreatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }
    @Override
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(i -> new InvoiceDto(i.getId(), i.getBillingRequestId(), i.getInvoiceNo(), i.getCreatedAt()))
                .collect(Collectors.toList());
    }
    @Override
    public InvoiceDto getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .map(i -> new InvoiceDto(i.getId(), i.getBillingRequestId(), i.getInvoiceNo(), i.getCreatedAt()))
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
}
