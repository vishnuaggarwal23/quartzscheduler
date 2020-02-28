package com.vishnu.aggarwal.quartz.rest.entity;

/*
Created by vishnu on 5/3/18 11:01 AM
*/

import com.vishnu.aggarwal.quartz.core.enums.Status;
import com.vishnu.aggarwal.quartz.rest.annotation.CustomUniqueUsername;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.annotations.FetchMode.SUBSELECT;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type User.
 */
@Entity
@Table(name = "USER")
@Getter
@Setter
@ToString(of = {"id", "username", "email", "status", "firstName", "lastName"})
public class User extends BaseEntity<Long> implements UserDetails, Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    @CustomUniqueUsername
    private String username;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean accountExpired = FALSE;
    @NotNull
    private Boolean accountLocked = FALSE;
    @NotNull
    private Boolean credentialsExpired = FALSE;
    @NotNull
    private Boolean accountEnabled = TRUE;
    @NotNull
    @Enumerated(STRING)
    private Status status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = EAGER)
    @Fetch(value = SUBSELECT)
    private List<UserAuthority> userAuthorities;

    /**
     * Gets authority names.
     *
     * @return the authority names
     */
    @Transient
    public List<String> getAuthorityNames() {
        Set<Authority> authorities = getAuthorityObjects();
        if (isEmpty(authorities)) {
            return null;
        }
        return authorities.stream().filter(Objects::nonNull).map(Authority::getName).collect(Collectors.toList());
    }

    /**
     * Gets authority objects.
     *
     * @return the authority objects
     */
    @Transient
    public Set<Authority> getAuthorityObjects() {
        return this.userAuthorities.stream().filter(Objects::nonNull).map(UserAuthority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorityObjects();
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

    /**
     * Gets full name.
     *
     * @return the full name
     */
    @Transient
    public String getFullName() {
        String fullName = EMPTY;
        if (isNotBlank(this.firstName)) {
            fullName = this.firstName;
        }
        if (isNotBlank(this.lastName)) {
            fullName = format("%s %s", fullName, this.lastName).trim();
        }
        return fullName;
    }

    /**
     * Has authority boolean.
     *
     * @param authority the authority
     * @return the boolean
     */
    @Transient
    public Boolean hasAuthority(String authority) {
        List<String> authorities = this.getAuthorityNames();
        return isEmpty(authorities) ? FALSE : authorities.contains(authority);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(username, user.username)
                .append(email, user.email)
                .append(password, user.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(username)
                .append(email)
                .append(password)
                .toHashCode();
    }
}
