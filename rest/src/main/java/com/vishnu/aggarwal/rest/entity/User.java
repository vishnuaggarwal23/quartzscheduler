package com.vishnu.aggarwal.rest.entity;

/*
Created by vishnu on 5/3/18 11:01 AM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USER")
@Getter
@Setter
@ToString
public class User extends BaseEntity<Long> implements UserDetails, Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @NotNull
    @NotEmpty
    private String username;
    @Email
    @NotNull
    @NotEmpty
    private String email;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    private Boolean accountExpired = FALSE;
    @NotNull
    private Boolean accountLocked = FALSE;
    @NotNull
    private Boolean credentialsExpired = FALSE;
    @NotNull
    private Boolean accountEnabled = TRUE;
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    protected Set<Role> roles = new HashSet<Role>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.accountEnabled;
    }
}
