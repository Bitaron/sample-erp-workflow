package com.company.erp.workflowManagement.controller;
import com.company.erp.workflowManagement.dto.*;
import com.company.erp.workflowManagement.service.WorkflowService;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(WorkflowController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class WorkflowControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private WorkflowService workflowService;
    @MockitoBean private JwtUtils jwtUtils;
    @Test
    @WithMockUser(roles = "ADMIN")
    void createWorkflow_AsAdmin_ReturnsCreated() throws Exception {
        WorkflowDefinitionResponseDto response = new WorkflowDefinitionResponseDto();
        response.setId(1L); response.setName("Billing Approval");
        when(workflowService.createWorkflow(any())).thenReturn(response);
        mockMvc.perform(post("/api/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Billing Approval\", \"documentType\":\"BILL_REQUEST\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void createWorkflow_AsNonAdmin_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/api/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Billing Approval\", \"documentType\":\"BILL_REQUEST\"}"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void addWorkflowStep_AsAdmin_ReturnsCreated() throws Exception {
        WorkflowStepResponseDto response = new WorkflowStepResponseDto(1L, 1, "Sales");
        when(workflowService.addWorkflowStep(eq(1L), any())).thenReturn(response);
        mockMvc.perform(post("/api/workflows/1/steps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"departmentId\":1, \"sequenceOrder\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentName").value("Sales"));
    }
    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void getWorkflows_ReturnsList() throws Exception {
        WorkflowDefinitionResponseDto response = new WorkflowDefinitionResponseDto();
        response.setName("Billing");
        when(workflowService.getAllWorkflows()).thenReturn(List.of(response));
        mockMvc.perform(get("/api/workflows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Billing"));
    }
}
