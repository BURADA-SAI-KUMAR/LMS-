package com.student.lms.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
    private String mfaCode; // optional for MFA
}
