package com.vishnu.aggarwal.quartz.rest.document;

/*
Created by vishnu on 23/5/18 3:14 PM
*/

import com.vishnu.aggarwal.quartz.rest.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.annotation.*;

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
