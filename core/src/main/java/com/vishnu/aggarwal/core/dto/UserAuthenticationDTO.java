package com.vishnu.aggarwal.core.dto;

/*
Created by vishnu on 4/8/18 6:24 PM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static java.lang.Boolean.FALSE;

@Getter
@Setter
@ToString
public class UserAuthenticationDTO {
    private UserDTO user;
    private Boolean isAuthenticated = FALSE;
    private String xAuthToken;

    public UserAuthenticationDTO() {
    }

    public UserAuthenticationDTO(UserDTO user, Boolean isAuthenticated, String xAuthToken) {
        this.user = user;
        this.isAuthenticated = isAuthenticated;
        this.xAuthToken = xAuthToken;
    }
}
