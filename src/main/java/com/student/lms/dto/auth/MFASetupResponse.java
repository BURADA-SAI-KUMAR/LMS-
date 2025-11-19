package com.student.lms.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MFASetupResponse {
	
	
    private String qrCodeUrl;
    private String secret;
    

}
