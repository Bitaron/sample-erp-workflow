package com.company.erp.userManagement.service.impl;
import com.company.erp.common.exception.UnauthorizedException;
import com.company.erp.security.JwtUtils;
import com.company.erp.userManagement.dto.*;
import com.company.erp.userManagement.entity.*;
import com.company.erp.userManagement.repository.*;
import com.company.erp.userManagement.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    public AuthServiceImpl(UserRepository userRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository; this.departmentRepository = departmentRepository; this.roleRepository = roleRepository; this.passwordEncoder = passwordEncoder; this.jwtUtils = jwtUtils;
    }
    @Override
    public RegisterUserResponseDto register(RegisterUserRequestDto request) {
        Department dep = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));
        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new RuntimeException("Role not found"));
        User user = new User();
        user.setName(request.getName());
        user.setDepartment(dep);
        user.setRole(role);
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        return new RegisterUserResponseDto(user.getId(), user.getName(), dep.getId(), role.getId(), user.getUserName());
    }
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(user.getUserName(), user.getRole().getName());
        return new LoginResponseDto(token);
    }
    @Override
    public CurrentUserResponseDto getCurrentUser(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UnauthorizedException("User not found"));
        return new CurrentUserResponseDto(user.getId(), user.getName(), user.getRole() != null ? user.getRole().getName() : null, user.getDepartment() != null ? user.getDepartment().getName() : null);
    }
}
