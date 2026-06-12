package com.company.erp.userManagement.controller;
import com.company.erp.userManagement.dto.DepartmentDto;
import com.company.erp.userManagement.dto.CreateDepartmentRequestDto;
import com.company.erp.userManagement.service.DepartmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) { this.departmentService = departmentService; }
    @GetMapping
    public List<DepartmentDto> getDepartments() {
        return departmentService.getAllDepartments();
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DepartmentDto createDepartment(@RequestBody CreateDepartmentRequestDto request) {
        return departmentService.createDepartment(request);
    }
}
