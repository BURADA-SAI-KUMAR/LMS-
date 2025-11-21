package com.student.lms.controllers;

import com.student.lms.entities.*;
import com.student.lms.services.RoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // ===================== 1. LIST ALL ROLES =====================
    @GetMapping
    public ResponseEntity<Set<RoleEnum>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // ===================== 2. GET PERMISSIONS OF ROLE =====================
    @GetMapping("/{role}/permissions")
    public ResponseEntity<Set<PermissionEnum>> getRolePermissions(@PathVariable RoleEnum role) {
        return ResponseEntity.ok(roleService.getPermissionsByRole(role));
    }
}
