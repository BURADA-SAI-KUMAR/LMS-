package com.student.lms.response;

import com.student.lms.entities.PermissionEnum;
import com.student.lms.entities.RoleEnum;
import lombok.Data;
import java.util.Set;

@Data
public class RoleResponse {
    private RoleEnum roleName;
    private Set<PermissionEnum> permissions;
}

