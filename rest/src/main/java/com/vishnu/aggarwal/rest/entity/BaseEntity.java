package com.vishnu.aggarwal.rest.entity;

/*
Created by vishnu on 1/3/18 1:23 PM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

import static java.lang.Boolean.FALSE;
import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID> implements Serializable {

    private static final long serialVersionUID = -2248190721476487645L;
    private Boolean isDeleted = FALSE;
    @Version
    private Long version;
    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date createdDate;
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date lastModifiedDate;
    @CreatedBy
    private User createdBy;
    @LastModifiedBy
    private User lastModifiedBy;

    protected abstract ID getId();

}
