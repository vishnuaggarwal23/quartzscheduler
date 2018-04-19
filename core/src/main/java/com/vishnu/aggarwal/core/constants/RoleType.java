package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 6/3/18 10:55 AM
*/

import java.util.Arrays;
import java.util.List;

/**
 * The type Role type.
 */
public class RoleType {
    /**
     * The constant ROLE_USER.
     */
    public static final String ROLE_USER = com.vishnu.aggarwal.core.enums.RoleType.ROLE_USER.toString();
    /**
     * The constant ROLE_ADMIN.
     */
    public static final String ROLE_ADMIN = com.vishnu.aggarwal.core.enums.RoleType.ROLE_ADMIN.toString();

    /**
     * Gets values.
     *
     * @return the values
     */
    public static List<String> getValues() {
        return Arrays.asList(ROLE_ADMIN, ROLE_USER);
    }
}
