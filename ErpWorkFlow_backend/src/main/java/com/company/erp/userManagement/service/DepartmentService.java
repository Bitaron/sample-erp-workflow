package com.company.erp.userManagement.service;
import com.company.erp.userManagement.dto.DepartmentDto;
import java.util.List;
public interface DepartmentService {
    List<DepartmentDto> getAllDepartments();
    DepartmentDto createDepartment(com.company.erp.userManagement.dto.CreateDepartmentRequestDto request);
}
