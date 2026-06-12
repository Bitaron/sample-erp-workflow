package com.company.erp.userManagement.repository;
import com.company.erp.userManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByName(String name);
}
