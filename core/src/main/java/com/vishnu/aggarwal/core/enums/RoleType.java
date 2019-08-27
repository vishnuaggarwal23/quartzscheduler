package com.vishnu.aggarwal.core.enums;

import lombok.Getter;

/**
 * The enum Role type.
 */

@Getter
public enum RoleType {
    /**
     * Role admin role type.
     */
    ROLE_ADMIN(com.vishnu.aggarwal.core.constants.RoleType.ROLE_ADMIN),
    /**
     * Role user role type.
     */
    ROLE_USER(com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER);

    private String role;

    RoleType(String role) {
        this.role = role;
    }
}