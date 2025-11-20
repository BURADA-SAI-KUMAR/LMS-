package com.student.lms.controllers;

import com.student.lms.dto.auth.*;
import com.student.lms.services.AuthService;
import com.student.lms.services.MFAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MFAService mfaService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Registration successful. Check email for verification token.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerifyRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/mfa/setup")
    public ResponseEntity<MFASetupResponse> setupMFA(@RequestParam String email) {
        return ResponseEntity.ok(mfaService.setupMFA(email));
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<String> verifyMFA(@RequestBody MFAVerifyRequest request) {
        mfaService.verifyMFA(request);
        return ResponseEntity.ok("MFA verified successfully");
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.forgotPassword(email);
        return ResponseEntity.ok("Password reset token sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful.");
    }

}
