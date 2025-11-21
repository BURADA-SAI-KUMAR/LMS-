package com.student.lms.controllers;

import com.student.lms.entities.RoleEnum;
import com.student.lms.entities.User;
import com.student.lms.services.RoleService;
import com.student.lms.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    private final RoleService roleService;

    // ================== CREATE ==================
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, 
                                           @RequestParam String role) {
        User createdUser = userService.createUser(user, role);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // ================== READ ==================
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================== UPDATE ==================
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // ================== DELETE ==================
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User Delted Successfully");
    }

    // ================== STATUS UPDATE ==================
    @PatchMapping("/{id}/status")
    public ResponseEntity<User> updateUserStatus(@PathVariable Long id,
                                                 @RequestParam String status) {
        User user = userService.updateUserStatus(id, status);
        return ResponseEntity.ok(user);
    }

    // ================== ROLE ASSIGNMENT ==================
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> assignRoleToUser(@PathVariable Long id,
                                                 @RequestParam String role) {
        User user = userService.assignRoleToUser(id, role);
        return ResponseEntity.ok(user);
    }

    // ================== EMAIL VERIFICATION ==================
    @PatchMapping("/{id}/verify-email")
    public ResponseEntity<User> verifyEmail(@PathVariable Long id) {
        User user = userService.verifyEmail(id);
        return ResponseEntity.ok(user);
    }

    // ================== MFA VERIFICATION ==================
    @PatchMapping("/{id}/verify-mfa")
    public ResponseEntity<User> verifyMfa(@PathVariable Long id) {
        User user = userService.verifyMfa(id);
        return ResponseEntity.ok(user);
    }
    
    


    // ===================== 3. ASSIGN ROLE TO USER =====================
    @PostMapping("/{id}/roles")
    public ResponseEntity<User> assignRole(@PathVariable Long id,
                                           @RequestParam RoleEnum role) {

        User updatedUser = roleService.assignRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }

    // ===================== 4. REMOVE ROLE FROM USER =====================
    @DeleteMapping("/{id}/roles/{role}")
    public ResponseEntity<User> removeRole(@PathVariable Long id,
                                           @PathVariable RoleEnum role) {

        User updatedUser = roleService.removeRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }
}
