package com.student.lms.dto.auth;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private String token;
}
