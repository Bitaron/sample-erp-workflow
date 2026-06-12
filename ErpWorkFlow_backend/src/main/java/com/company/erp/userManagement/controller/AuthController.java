package com.company.erp.userManagement.controller;
import com.company.erp.userManagement.dto.*;
import com.company.erp.userManagement.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/register")
    public RegisterUserResponseDto register(@RequestBody RegisterUserRequestDto request) {
        return authService.register(request);
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
    @GetMapping("/me")
    public CurrentUserResponseDto getCurrentUser(Principal principal) {
        return authService.getCurrentUser(principal.getName());
    }
}
