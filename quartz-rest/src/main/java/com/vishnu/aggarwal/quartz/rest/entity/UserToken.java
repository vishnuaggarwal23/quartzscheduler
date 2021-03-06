package com.vishnu.aggarwal.quartz.rest.entity;

import com.vishnu.aggarwal.quartz.core.enums.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

/**
 * The type User token.
 */
/*
Created by vishnu on 20/4/18 11:10 AM
*/
@Entity
@Table(name = "USER_TOKEN")
@Getter
@Setter
@ToString(of = {"id", "user", "token", "status"})
public class UserToken extends BaseEntity<Long> implements Serializable {
    private static final long serialVersionUID = -4720986982733184274L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(fetch = EAGER)
    @NotNull
    private User user;

    @JoinColumn(name = "tokenId", referencedColumnName = "id")
    @ManyToOne(fetch = EAGER)
    @NotNull
    private Token token;

    @NotNull
    @Enumerated(STRING)
    private Status status;

    public static UserToken getInstance() {
        return new UserToken();
    }
}
