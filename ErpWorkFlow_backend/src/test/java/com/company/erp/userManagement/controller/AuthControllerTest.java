package com.company.erp.userManagement.controller;

import com.company.erp.userManagement.dto.LoginRequestDto;
import com.company.erp.userManagement.dto.RegisterUserRequestDto;
import com.company.erp.userManagement.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.company.erp.security.JwtUtils;
import com.company.erp.security.JwtAuthenticationFilter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.context.annotation.Import;
import com.company.erp.security.SecurityConfig;
import com.company.erp.security.JwtAuthenticationFilter;
import com.company.erp.security.JwtUtils;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtils.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void registerUser_ReturnsCreatedUser() throws Exception {
        String requestBody = """
                {
                    "name": "John Doe",
                    "departmentId": 1,
                    "roleId": 1,
                    "userName": "john",
                    "password": "password123"
                }
                """;

        when(authService.register(any(RegisterUserRequestDto.class))).thenReturn(
                new com.company.erp.userManagement.dto.RegisterUserResponseDto(1L, "John Doe", 1L, 1L, "john")
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("john"));
    }

    @Test
    void loginSuccess_ReturnsToken() throws Exception {
        String requestBody = """
                {
                    "userName": "john",
                    "password": "password123"
                }
                """;

        when(authService.login(any(LoginRequestDto.class))).thenReturn(
                new com.company.erp.userManagement.dto.LoginResponseDto("jwt-token-123")
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }

    @Test
    void loginFailure_ReturnsUnauthorized() throws Exception {
        String requestBody = """
                {
                    "userName": "john",
                    "password": "wrongpassword"
                }
                """;

        when(authService.login(any(LoginRequestDto.class)))
                .thenThrow(new com.company.erp.common.exception.UnauthorizedException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @org.springframework.security.test.context.support.WithMockUser(username="admin", roles="ADMIN")
    void getCurrentUser_ReturnsUser() throws Exception {
        when(authService.getCurrentUser("admin")).thenReturn(
                new com.company.erp.userManagement.dto.CurrentUserResponseDto(1L, "Admin User", "ADMIN", "Sales")
        );
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.department").value("Sales"));
    }

}
