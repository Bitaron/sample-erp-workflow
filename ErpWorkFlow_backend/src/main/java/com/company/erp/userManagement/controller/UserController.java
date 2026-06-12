package com.company.erp.userManagement.controller;
import com.company.erp.userManagement.dto.UserDto;
import com.company.erp.userManagement.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }
    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }
}
