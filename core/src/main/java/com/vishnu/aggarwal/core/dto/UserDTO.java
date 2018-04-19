package com.vishnu.aggarwal.core.dto;

/*
Created by vishnu on 5/3/18 11:01 AM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * The type User dto.
 */
@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean accountExpired = FALSE;
    private Boolean accountLocked = FALSE;
    private Boolean credentialsExpired = FALSE;
    private Boolean accountEnabled = TRUE;
    private Boolean isDeleted = FALSE;
    private Set<RoleDTO> roles;

    /**
     * Is account non expired boolean.
     *
     * @return the boolean
     */
    public Boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    /**
     * Is account non locked boolean.
     *
     * @return the boolean
     */
    public Boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    /**
     * Is credentials non expired boolean.
     *
     * @return the boolean
     */
    public Boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public Boolean isEnabled() {
        return this.accountEnabled;
    }
}
