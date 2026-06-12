package com.company.erp.invoiceManagement.repository;
import com.company.erp.invoiceManagement.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByBillingRequestId(Long billingRequestId);
}
