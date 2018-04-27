package com.vishnu.aggarwal.rest.entity;

/*
Created by vishnu on 21/4/18 1:59 PM
*/

import com.vishnu.aggarwal.core.util.DateUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.Objects;

import static com.vishnu.aggarwal.core.util.DateUtils.getEnd;
import static java.util.Objects.nonNull;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "TOKEN")
@Getter
@Setter
@ToString(of={"id", "token", "issueId", "expirationDate", "issuedDate"})
public class Token extends BaseEntity<Long> implements org.springframework.security.core.token.Token{

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String token;

    private Date expirationDate;

    @NotNull
    private Date issuedDate;

    @NotNull
    @NotBlank
    private String issueId;

    @Override
    public String getKey() {
        return this.token;
    }

    @Override
    @Transient
    public long getKeyCreationTime() {
        return this.issuedDate.getTime();
    }

    @Override
    @Transient
    public String getExtendedInformation() {
        return null;
    }

    public void setExpirationDate(Date expirationDate) {
        if(nonNull(expirationDate)) {
            this.expirationDate = getEnd(expirationDate);
        }
    }
}
