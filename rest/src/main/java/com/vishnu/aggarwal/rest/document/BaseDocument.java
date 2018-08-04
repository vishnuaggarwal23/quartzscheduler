package com.vishnu.aggarwal.rest.document;

/*
Created by vishnu on 23/5/18 3:14 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@CommonsLog
@ToString
public abstract class BaseDocument<ID> implements Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    protected abstract ID getId();
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

}
