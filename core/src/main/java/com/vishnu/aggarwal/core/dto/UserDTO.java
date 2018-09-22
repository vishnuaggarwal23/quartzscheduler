package com.vishnu.aggarwal.core.dto;

/*
Created by vishnu on 5/3/18 11:01 AM
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
    private String firstName;
    private String lastName;
    private List<AuthorityDTO> roles;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String password, Boolean accountExpired, Boolean accountLocked, Boolean credentialsExpired, Boolean accountEnabled, Boolean isDeleted, String firstName, String lastName, List<AuthorityDTO> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.accountEnabled = accountEnabled;
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public UserDTO(Long id) {
        this.id = id;
    }

    public UserDTO(String id) {
        this.id = valueOf(id);
    }

    /**
     * Is account non expired boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public Boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    /**
     * Is account non locked boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public Boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    /**
     * Is credentials non expired boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public Boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public Boolean isEnabled() {
        return this.accountEnabled;
    }

    @JsonInclude
    public String getFullName() {
        String fullName = EMPTY;
        if (isNotBlank(this.firstName)) {
            fullName = this.firstName;
        }
        if (isNotBlank(this.lastName)) {
            fullName = format("%s %s", fullName, this.lastName);
        }
        return fullName;
    }
}
