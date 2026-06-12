package com.company.erp.workflowManagement.controller;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.service.WorkflowInstanceService;
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
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(WorkflowInstanceController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class WorkflowInstanceControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private WorkflowInstanceService service;
    @MockitoBean private JwtUtils jwtUtils;
    @Test
    @WithMockUser
    void startWorkflow_ReturnsInProgress() throws Exception {
        when(service.startWorkflow(any())).thenReturn(new WorkflowInstanceResponseDto(1L, "IN_PROGRESS"));
        mockMvc.perform(post("/api/workflow-instances/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"documentId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
    @Test
    @WithMockUser
    void approveWorkflow_ReturnsInProgressOrCompleted() throws Exception {
        when(service.approveWorkflow(1L)).thenReturn(new WorkflowInstanceResponseDto(1L, "COMPLETED"));
        mockMvc.perform(post("/api/workflow-instances/1/approve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
    @Test
    @WithMockUser
    void getAuditLogs_ReturnsLogs() throws Exception {
        when(service.getAuditLogs(1L)).thenReturn(List.of(new WorkflowAuditLogDto("john", "APPROVED", LocalDateTime.now())));
        mockMvc.perform(get("/api/workflow-instances/1/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].action").value("APPROVED"));
    }
}
