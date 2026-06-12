package com.company.erp.userManagement.controller;
import com.company.erp.userManagement.dto.RoleDto;
import com.company.erp.userManagement.service.RoleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) { this.roleService = roleService; }
    @GetMapping
    public List<RoleDto> getRoles() {
        return roleService.getAllRoles();
    }
}
