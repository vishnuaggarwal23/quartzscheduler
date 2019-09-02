package com.vishnu.aggarwal.core.constants;

/*
Created by vishnu on 6/3/18 10:55 AM
*/

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class RoleType {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static List<String> getValues() {
        return Arrays.asList(ROLE_ADMIN, ROLE_USER);
    }
}
