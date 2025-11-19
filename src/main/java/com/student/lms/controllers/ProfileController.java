package com.student.lms.controllers;

import com.student.lms.dto.profile.ChangePasswordRequest;
import com.student.lms.dto.profile.UpdateProfileRequest;
import com.student.lms.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestParam String email, @RequestBody UpdateProfileRequest request) {
        profileService.updateProfile(email, request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String email, @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(email, request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
