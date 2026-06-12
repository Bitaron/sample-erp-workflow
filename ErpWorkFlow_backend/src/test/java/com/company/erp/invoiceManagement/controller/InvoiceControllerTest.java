package com.company.erp.invoiceManagement.controller;
import com.company.erp.invoiceManagement.dto.InvoiceDto;
import com.company.erp.invoiceManagement.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.company.erp.security.SecurityConfig;
import com.company.erp.security.JwtAuthenticationFilter;
import com.company.erp.security.JwtUtils;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(InvoiceController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class InvoiceControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private InvoiceService service;
    @MockitoBean private JwtUtils jwtUtils;
    @Test
    @WithMockUser
    void getInvoices_ReturnsInvoices() throws Exception {
        when(service.getAllInvoices()).thenReturn(List.of(new InvoiceDto(1L, 100L, "INV-123", LocalDateTime.now())));
        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNo").value("INV-123"));
    }
}
