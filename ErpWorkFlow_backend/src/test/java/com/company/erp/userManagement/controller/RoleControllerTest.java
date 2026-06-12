package com.company.erp.userManagement.controller;
import com.company.erp.userManagement.dto.RoleDto;
import com.company.erp.userManagement.service.RoleService;
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
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(RoleController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class RoleControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private RoleService roleService;
    @MockitoBean private JwtUtils jwtUtils;
    @Test
    @WithMockUser
    void getRoles_ReturnsRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(List.of(new RoleDto(1L, "ADMIN")));
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }
}
