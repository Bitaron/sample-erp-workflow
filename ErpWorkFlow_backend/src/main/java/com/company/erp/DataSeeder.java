package com.company.erp;
import com.company.erp.userManagement.entity.*;
import com.company.erp.userManagement.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class DataSeeder implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public DataSeeder(DepartmentRepository dr, RoleRepository rr, UserRepository ur, PasswordEncoder pe) {
        this.departmentRepository = dr; this.roleRepository = rr; this.userRepository = ur; this.passwordEncoder = pe;
    }
    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0) {
            departmentRepository.save(new Department("Sales"));
            departmentRepository.save(new Department("Accounts"));
        }
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("SALES_EXECUTIVE"));
            roleRepository.save(new Role("ACCOUNTS_EXECUTIVE"));
        }
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Default Admin");
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(roleRepository.findByName("ADMIN"));
            userRepository.save(admin);
        }
    }
}
