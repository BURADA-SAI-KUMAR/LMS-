package com.student.lms.config;

import com.student.lms.entities.RoleEnum;
import com.student.lms.entities.User;
import com.student.lms.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        if (userRepository.count() == 0) {

            User superAdmin = new User();
            superAdmin.setFullName("System Super Admin");
            superAdmin.setFirstName("System");
            superAdmin.setLastName("Admin");
            superAdmin.setEmail("admin@system.com");
            superAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            superAdmin.setRole(RoleEnum.SUPER_ADMIN);
            superAdmin.setEmailVerified(true);
            superAdmin.setStatus("ACTIVE");
            superAdmin.setMfaEnabled(false);
            superAdmin.setMfaVerified(false);
            superAdmin.setCreatedAt(LocalDateTime.now());
            superAdmin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(superAdmin);

            System.out.println("✔ Default SUPER_ADMIN created: admin@system.com / Admin@123");
        }
    }
}
