package com.student.lms.services;

import com.student.lms.entities.*;
import com.student.lms.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    // List all roles
    public Set<RoleEnum> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());
    }

    // Get permissions of a role
    public Set<PermissionEnum> getPermissionsByRole(RoleEnum role) {
        Role existingRole = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));

        return existingRole.getPermissions();
    }

    // Assign role to user
    public User assignRole(Long userId, RoleEnum role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        user.setRole(role);
        return userRepository.save(user);
    }

    // Remove role (reset to GUEST)
    public User removeRole(Long userId, RoleEnum role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if (!user.getRole().equals(role)) {
            throw new RuntimeException("User does not have the given role!");
        }

        // default fallback role
        user.setRole(RoleEnum.GUEST);

        return userRepository.save(user);
    }
}
