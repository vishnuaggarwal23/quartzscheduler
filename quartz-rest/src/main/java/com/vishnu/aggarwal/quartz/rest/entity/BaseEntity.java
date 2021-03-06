package com.vishnu.aggarwal.quartz.rest.entity;

/*
Created by vishnu on 1/3/18 1:23 PM
*/

import lombok.*;
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

/**
 * The type Base entity.
 *
 * @param <ID> the type parameter
 */
@Getter
@Setter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
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

    /**
     * Gets id.
     *
     * @return the id
     */
    protected abstract ID getId();

}
