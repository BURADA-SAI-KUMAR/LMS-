package com.student.lms.services;

import com.student.lms.dto.auth.MFASetupResponse;
import com.student.lms.dto.auth.MFAVerifyRequest;
import com.student.lms.entities.MFASecret;
import com.student.lms.entities.User;
import com.student.lms.repositories.MFASecretRepository;
import com.student.lms.repositories.UserRepository;
import com.student.lms.util.QRCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MFAService {

    private final MFASecretRepository mfaSecretRepository;
    private final UserRepository userRepository;

    // Generate MFA secret and QR code
    public MFASetupResponse setupMFA(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String secretKey = Base64.getEncoder().encodeToString(
                (email + System.currentTimeMillis()).getBytes()
        );

        MFASecret mfaSecret = MFASecret.builder()
                .user(user)
                .secretKey(secretKey)
                .build();

        mfaSecretRepository.save(mfaSecret);

        String qrCodeUrl = QRCodeGenerator.generateQRCode("otpauth://totp/LMS:" + email + "?secret=" + secretKey + "&issuer=LMS");

        MFASetupResponse response = new MFASetupResponse();
        response.setQrCodeUrl(qrCodeUrl);
        response.setSecret(secretKey);
        return response;
    }

    // Verify MFA code (dummy verification here, you can integrate Google Authenticator lib)
    public void verifyMFA(MFAVerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        MFASecret mfaSecret = mfaSecretRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("MFA not setup"));

        if (!request.getCode().equals("123456")) { // Dummy code, replace with real TOTP check
            throw new RuntimeException("Invalid MFA code");
        }

        user.setMfaVerified(true);
        userRepository.save(user);
    }
}
