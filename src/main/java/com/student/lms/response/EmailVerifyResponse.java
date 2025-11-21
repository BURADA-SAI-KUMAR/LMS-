package com.student.lms.response;

import com.student.lms.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerifyResponse {
    private String message;
    private User user;
}
