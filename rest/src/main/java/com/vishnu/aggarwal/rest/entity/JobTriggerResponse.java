package com.vishnu.aggarwal.rest.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "QRTZ_TRIGGERS_RESPONSE")
@Getter
@Setter
public class JobTriggerResponse implements Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String triggerName;
    private String jobName;
    private Integer responseCode;
    private String responseHeader;
    private String responseBody;
    private Date fireTime;

}
