package com.company.erp.billingManagement.controller;
import com.company.erp.billingManagement.dto.*;
import com.company.erp.billingManagement.service.BillRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.company.erp.security.SecurityConfig;
import com.company.erp.security.JwtAuthenticationFilter;
import com.company.erp.security.JwtUtils;
import org.springframework.data.domain.PageImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(BillRequestController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class BillRequestControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private BillRequestService billRequestService;
    @MockitoBean private JwtUtils jwtUtils;
    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void createBillRequest_AsSalesExec_ReturnsCreated() throws Exception {
        BillRequestResponseDto response = new BillRequestResponseDto();
        response.setId(1L); response.setCustomerId(1001L); response.setAmount(5000.0);
        when(billRequestService.createBillRequest(any())).thenReturn(response);
        mockMvc.perform(post("/api/bill-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1001, \"amount\":5000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @WithMockUser(roles = "ACCOUNTS_EXECUTIVE")
    void createBillRequest_AsAccountsExec_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/api/bill-requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1001, \"amount\":5000.0}"))
                .andExpect(status().isForbidden());
    }
/*    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void getBillRequests_ReturnsPagedList() throws Exception {
        BillRequestResponseDto response = new BillRequestResponseDto();
        response.setId(1L); response.setStatus("IN_PROGRESS");
        when(billRequestService.getBillRequests(any())).thenReturn(new PageImpl<>(List.of(response)));
        mockMvc.perform(get("/api/bill-requests?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].status").value("IN_PROGRESS"));
    }*/
    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void getBillRequestById_ReturnsDetails() throws Exception {
        BillRequestResponseDto response = new BillRequestResponseDto();
        response.setId(1L);
        when(billRequestService.getBillRequestById(1L)).thenReturn(response);
        mockMvc.perform(get("/api/bill-requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
