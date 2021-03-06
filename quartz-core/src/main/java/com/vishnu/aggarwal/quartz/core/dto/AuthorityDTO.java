package com.vishnu.aggarwal.quartz.core.dto;

import com.vishnu.aggarwal.quartz.core.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
Created by vishnu on 5/3/18 11:11 AM
*/

/**
 * The type Role dto.
 */
@Getter
@Setter
@ToString
public class AuthorityDTO {
    private Long id;
    private String authority;
    private Status status;

    public AuthorityDTO() {

    }

    public AuthorityDTO(Long id, String authority, Status status) {
        this.id = id;
        this.authority = authority;
        this.status = status;
    }
}
