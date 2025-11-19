package com.student.lms.dto.auth;

import lombok.Data;

@Data
public class MFAVerifyRequest {
    private String email;
    private String code;
}
