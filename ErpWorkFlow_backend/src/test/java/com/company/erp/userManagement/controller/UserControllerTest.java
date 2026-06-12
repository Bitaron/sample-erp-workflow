package com.company.erp.userManagement.controller;

import com.company.erp.userManagement.dto.UserDto;
import com.company.erp.userManagement.service.UserService;
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

@WebMvcTest(UserController.class)
@org.springframework.context.annotation.Import({com.company.erp.security.SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_WithAdminRole_ReturnsUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                new UserDto(1L, "John", "Sales", "SALES_EXECUTIVE", "john")
        ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userName").value("john"));
    }

    @Test
    @WithMockUser(roles = "SALES_EXECUTIVE")
    void getUsers_WithNonAdminRole_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }
}
