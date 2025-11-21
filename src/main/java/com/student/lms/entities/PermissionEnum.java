package com.student.lms.entities;

public enum PermissionEnum {
    USER_CREATE,
    USER_EDIT,
    USER_EDIT_SELF,      // INSTRUCTOR can edit only self
    USER_DELETE,
    ROLE_ASSIGN,
    ROLE_VIEW,
    USER_VIEW,
    USER_VIEW_SELF,      // STUDENT can view own info
    USER_VIEW_ENROLLED,  // INSTRUCTOR can view enrolled students
    USER_VIEW_TEAM,      // MANAGER can view team
    VIEW_ANALYTICS
}
