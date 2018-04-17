package com.vishnu.aggarwal.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import static java.lang.Boolean.FALSE;

/*
Created by vishnu on 5/3/18 11:11 AM
*/

@Getter
@Setter
@ToString
public class RoleDTO {
    private Long id;
    private String authority;
    private Set<UserDTO> users;
    private Boolean isDeleted = FALSE;
}
