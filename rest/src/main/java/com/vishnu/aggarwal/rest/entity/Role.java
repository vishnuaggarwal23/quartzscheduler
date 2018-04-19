package com.vishnu.aggarwal.rest.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

/*
Created by vishnu on 5/3/18 11:11 AM
*/

@Entity
@Table(name = "ROLE")
@Getter
@Setter
@ToString
public class Role extends BaseEntity<Long> implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
