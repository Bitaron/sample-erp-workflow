package com.company.erp.userManagement.service;
import com.company.erp.userManagement.dto.*;
public interface AuthService {
    RegisterUserResponseDto register(RegisterUserRequestDto request);
    LoginResponseDto login(LoginRequestDto request);
    CurrentUserResponseDto getCurrentUser(String username);
}
