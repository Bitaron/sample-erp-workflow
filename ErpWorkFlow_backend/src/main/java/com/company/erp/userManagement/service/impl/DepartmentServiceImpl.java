package com.company.erp.userManagement.service.impl;
import com.company.erp.userManagement.dto.DepartmentDto;
import com.company.erp.userManagement.entity.Department;
import com.company.erp.userManagement.repository.DepartmentRepository;
import com.company.erp.userManagement.service.DepartmentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) { this.departmentRepository = departmentRepository; }
    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(d -> new DepartmentDto(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public DepartmentDto createDepartment(com.company.erp.userManagement.dto.CreateDepartmentRequestDto request) {
        Department dept = new Department();
        dept.setName(request.getName());
        dept = departmentRepository.save(dept);
        return new DepartmentDto(dept.getId(), dept.getName());
    }
}
