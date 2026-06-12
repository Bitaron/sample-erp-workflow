package com.company.erp.userManagement.controller;

import com.company.erp.userManagement.dto.DepartmentDto;
import com.company.erp.userManagement.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.company.erp.security.JwtUtils;
import com.company.erp.security.JwtAuthenticationFilter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.context.annotation.Import;
import com.company.erp.security.SecurityConfig;
import com.company.erp.security.JwtAuthenticationFilter;
import com.company.erp.security.JwtUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@WebMvcTest(DepartmentController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepartmentService departmentService;

    @Test
    @WithMockUser
    void getDepartments_WithAuthenticatedUser_ReturnsDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(List.of(
                new DepartmentDto(1L, "Sales"),
                new DepartmentDto(2L, "Accounts")
        ));

        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Sales"));
    }

    @Test
    void getDepartments_WithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/departments"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @org.springframework.security.test.context.support.WithMockUser(roles="ADMIN")
    void createDepartment_AsAdmin_ReturnsCreated() throws Exception {
        com.company.erp.userManagement.dto.DepartmentDto response = new com.company.erp.userManagement.dto.DepartmentDto(3L, "HR");
        when(departmentService.createDepartment(org.mockito.ArgumentMatchers.any())).thenReturn(response);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/departments")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"HR\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

}
