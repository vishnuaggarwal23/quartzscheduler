package com.vishnu.aggarwal.rest.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

/*
Created by vishnu on 20/4/18 10:39 AM
*/

@Entity
@Table(name = "AUTHORITY")
@Getter
@Setter
@ToString(of = {"id", "name"})
public class Authority extends BaseEntity<Long> implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = -4446929343152142811L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    @NotNull
    @NotBlank
    private String name;

    @Override
    @Transient
    public String getAuthority() {
        return this.name;
    }
}
