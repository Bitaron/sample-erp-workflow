package com.company.erp.userManagement.repository;
import com.company.erp.userManagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
