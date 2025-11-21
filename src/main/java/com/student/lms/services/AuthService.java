package com.student.lms.services;

import com.student.lms.dto.auth.*;
import com.student.lms.entities.*;
import com.student.lms.repositories.*;
import com.student.lms.response.UserLoginData;
import com.student.lms.security.JWTUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final EmailService emailService;
    private final MFAService mfaService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
     private final UserService userService;
     
     
     private String generateOtp() {
         return String.valueOf((int)(Math.random() * 900000) + 100000);  // 6-digit
     }

    // Register new user
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        RoleEnum role;
        try {
            role = RoleEnum.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role: " + request.getRole());
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())  // ⚠ raw password (UserService will encode it)
                .role(role)
                .emailVerified(false)
                .status("ACTIVE")
                .build();

        // ⬇️ IMPORTANT: Use UserService to create the user so audit logs are generated
        User savedUser = userService.createUser(user, request.getRole());

        // Email verification
//        String token = UUID.randomUUID().toString();
        String token = generateOtp();


   

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        verificationTokenRepository.save(verificationToken);

        emailService.sendEmailVerification(savedUser.getEmail(), token);
        
        return savedUser;  // ✅ return User
    }


    // Verify email
    public User  verifyEmail(EmailVerifyRequest request) {
        VerificationToken token = verificationTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        verificationTokenRepository.delete(token);
        
        return user;  
    }

    // Login
    public UserLoginData  login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        if (user.isMfaEnabled() && !user.isMfaVerified()) {
            return new UserLoginData("MFA_REQUIRED", user);  
        }

     
        String token = jwtUtil.generateToken(user.getEmail(), null);
        return new UserLoginData(token, user);
    }
    
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate token
        String token = UUID.randomUUID().toString();

        // Save or update token
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user)
                .orElse(PasswordResetToken.builder().user(user).build());

        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(passwordResetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken tokenEntity = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token expired");
        }

        // Update password
        User user = tokenEntity.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Delete token
        passwordResetTokenRepository.delete(tokenEntity);
    }

}
