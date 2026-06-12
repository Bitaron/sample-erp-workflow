package com.company.erp.userManagement.service.impl;
import com.company.erp.userManagement.dto.RoleDto;
import com.company.erp.userManagement.repository.RoleRepository;
import com.company.erp.userManagement.service.RoleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    public RoleServiceImpl(RoleRepository roleRepository) { this.roleRepository = roleRepository; }
    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(r -> new RoleDto(r.getId(), r.getName())).collect(Collectors.toList());
    }
}
