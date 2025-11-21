package com.student.lms.services;

import com.student.lms.entities.RoleEnum;
import com.student.lms.entities.User;
import com.student.lms.repositories.UserRepository;
import com.student.lms.repositories.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;
    private final VerificationTokenRepository verificationTokenRepository;

    // ================== CREATE ==================
    public User createUser(User user, String roleName) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleEnum.valueOf(roleName));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser,
                "CREATE",
                "User",
                "User created with email: " + savedUser.getEmail()
        );

        return savedUser;
    }

    // ================== READ ==================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ================== UPDATE ==================
    public User updateUser(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        StringBuilder changes = new StringBuilder();

        if (updatedUser.getFullName() != null &&
                !updatedUser.getFullName().equals(existingUser.getFullName())) {

            changes.append("FullName: ")
                    .append(existingUser.getFullName())
                    .append(" -> ")
                    .append(updatedUser.getFullName())
                    .append(". ");

            existingUser.setFullName(updatedUser.getFullName());
        }
        
        if (updatedUser.getFirstName() != null &&
                !updatedUser.getFirstName().equals(existingUser.getFirstName())) {

            changes.append("FirstName: ")
                    .append(existingUser.getFirstName())
                    .append(" -> ")
                    .append(updatedUser.getFirstName())
                    .append(". ");

            existingUser.setFirstName(updatedUser.getFirstName());
        }
        
        if (updatedUser.getLastName() != null &&
                !updatedUser.getLastName().equals(existingUser.getLastName())) {

            changes.append("LastName: ")
                    .append(existingUser.getLastName())
                    .append(" -> ")
                    .append(updatedUser.getLastName())
                    .append(". ");

            existingUser.setLastName(updatedUser.getLastName());
        }
        
        if (updatedUser.getEmail() != null &&
                !updatedUser.getEmail().equals(existingUser.getEmail())) {

            changes.append("Email: ")
                    .append(existingUser.getEmail())
                    .append(" -> ")
                    .append(updatedUser.getEmail())
                    .append(". ");

            existingUser.setEmail(updatedUser.getEmail());
        }
        
        if (updatedUser.getRole() != null &&
                !updatedUser.getRole().equals(existingUser.getRole())) {

            changes.append("Role: ")
                    .append(existingUser.getRole())
                    .append(" -> ")
                    .append(updatedUser.getRole())
                    .append(". ");

            existingUser.setRole(updatedUser.getRole());
        }

        if (updatedUser.getPassword() != null) {

            changes.append("Password updated. ");

            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.isMfaEnabled() != existingUser.isMfaEnabled()) {

            changes.append("MFA Enabled: ")
                    .append(existingUser.isMfaEnabled())
                    .append(" -> ")
                    .append(updatedUser.isMfaEnabled())
                    .append(". ");

            existingUser.setMfaEnabled(updatedUser.isMfaEnabled());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(existingUser);

        auditService.logAction(
                savedUser,
                "UPDATE",
                "User",
                changes.toString()
        );

        return savedUser;
    }

    // ================== DELETE ==================
    public User deleteUser(Long id) {
    	
    	// verificationTokenRepository.deleteByUserId(id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        userRepository.delete(user);

        auditService.logAction(
                user,
                "DELETE",
                "User",
                "User deleted with email: " + user.getEmail()
        );
        
        return user;
    }

    // ================== STATUS UPDATE ==================
    public User updateUserStatus(Long id, String status) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        String oldStatus = user.getStatus();

        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser,
                "STATUS_UPDATE",
                "User",
                "Status changed from " + oldStatus + " to " + status
        );

        return savedUser;
    }

    // ================== ROLE ASSIGNMENT ==================
    public User assignRoleToUser(Long userId, String roleName) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        String oldRole = user.getRole().name();

        user.setRole(RoleEnum.valueOf(roleName));
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser,
                "ROLE_ASSIGNMENT",
                "User",
                "Role changed from " + oldRole + " to " + roleName
        );

        return savedUser;
    }

    // ================== EMAIL VERIFICATION ==================
    public User verifyEmail(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser,
                "EMAIL_VERIFICATION",
                "User",
                "Email verified"
        );

        return savedUser;
    }

    // ================== MFA VERIFICATION ==================
    public User verifyMfa(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!user.isMfaEnabled()) {
            throw new IllegalStateException("MFA not enabled for user");
        }

        user.setMfaVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        auditService.logAction(
                savedUser,
                "MFA_VERIFICATION",
                "User",
                "MFA successfully verified"
        );

        return savedUser;
    }
}
