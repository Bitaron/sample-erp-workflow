package com.company.erp.userManagement.service.impl;
import com.company.erp.userManagement.dto.UserDto;
import com.company.erp.userManagement.repository.UserRepository;
import com.company.erp.userManagement.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getName(), u.getDepartment() != null ? u.getDepartment().getName() : null, u.getRole() != null ? u.getRole().getName() : null, u.getUserName()))
                .collect(Collectors.toList());
    }
}
