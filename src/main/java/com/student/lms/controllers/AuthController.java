package com.student.lms.controllers;

import com.student.lms.dto.auth.*;
import com.student.lms.entities.User;
import com.student.lms.response.EmailVerifyResponse;
import com.student.lms.response.LoginResponse;
import com.student.lms.response.RegisterResponse;
import com.student.lms.response.UserLoginData;
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

//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
//        authService.register(request);
//        return ResponseEntity.ok("Registration successful. Check email for verification token.");
//    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        User registeredUser = authService.register(request);

        RegisterResponse response = new RegisterResponse(
                "Registration successful. Check email for verification token.",
                registeredUser
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-email")
    public ResponseEntity<EmailVerifyResponse> verifyEmail(@RequestBody EmailVerifyRequest request) {
        
        User verifiedUser = authService.verifyEmail(request);

        EmailVerifyResponse response = new EmailVerifyResponse(
                "Email verified successfully",
                verifiedUser
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        UserLoginData loginData = authService.login(request);

        LoginResponse response = new LoginResponse(
                "Login successful",
                loginData.getToken(),
                loginData.getUser()
        );

        return ResponseEntity.ok(response);
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
