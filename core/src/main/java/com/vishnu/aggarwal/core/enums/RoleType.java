package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_ADMIN(com.vishnu.aggarwal.core.constants.RoleType.ROLE_ADMIN),
    ROLE_USER(com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER);

    private String role;

    RoleType(String role) {
        this.role = role;
    }
}