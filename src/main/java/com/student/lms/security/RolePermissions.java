package com.student.lms.security;

import com.student.lms.entities.PermissionEnum;
import com.student.lms.entities.RoleEnum;

import java.util.*;

public class RolePermissions {

    private static final Map<RoleEnum, Set<PermissionEnum>> permissionsMap = new HashMap<>();

    static {

        // ================= SUPER ADMIN =================
        permissionsMap.put(RoleEnum.SUPER_ADMIN, Set.of(
                PermissionEnum.USER_CREATE,
                PermissionEnum.USER_EDIT,
                PermissionEnum.USER_DELETE,
                PermissionEnum.ROLE_ASSIGN,
                PermissionEnum.ROLE_VIEW,
                PermissionEnum.USER_VIEW,
                PermissionEnum.USER_VIEW_TEAM,
                PermissionEnum.USER_VIEW_ENROLLED,
                PermissionEnum.USER_VIEW_SELF,
                PermissionEnum.USER_EDIT_SELF,
                PermissionEnum.VIEW_ANALYTICS
        ));

        // ================= ADMIN =================
        permissionsMap.put(RoleEnum.ADMIN, Set.of(
                PermissionEnum.USER_CREATE,
                PermissionEnum.USER_EDIT,
                PermissionEnum.USER_DELETE,
                PermissionEnum.ROLE_ASSIGN,
                PermissionEnum.ROLE_VIEW,
                PermissionEnum.USER_VIEW
        ));

        // ================= INSTRUCTOR =================
        permissionsMap.put(RoleEnum.INSTRUCTOR, Set.of(
                PermissionEnum.USER_VIEW_ENROLLED
        ));

        // ================= STUDENT =================
        permissionsMap.put(RoleEnum.STUDENT, Set.of(
                PermissionEnum.USER_VIEW_SELF,
                PermissionEnum.USER_EDIT_SELF
        ));

        // ================= MANAGER =================
        permissionsMap.put(RoleEnum.MANAGER, Set.of(
        		 PermissionEnum.USER_VIEW,
                PermissionEnum.USER_VIEW_TEAM,
                PermissionEnum.USER_CREATE // LIMITED: Manager can only create students
        ));

        // ================= GUEST =================
        permissionsMap.put(RoleEnum.GUEST, Set.of()); // no permissions
    }

    public static Set<PermissionEnum> getPermissions(RoleEnum role) {
        return permissionsMap.getOrDefault(role, Set.of());
    }
}
