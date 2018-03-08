package com.vishnu.aggarwal.core.config;

/*
Created by vishnu on 6/3/18 10:55 AM
*/

import java.util.Arrays;
import java.util.List;

public class RoleType {
    public static final String ROLE_USER = com.vishnu.aggarwal.core.enums.RoleType.ROLE_USER.toString();
    public static final String ROLE_ADMIN = com.vishnu.aggarwal.core.enums.RoleType.ROLE_ADMIN.toString();

    public static List<String> getValues() {
        return Arrays.asList(ROLE_ADMIN, ROLE_USER);
    }


}
