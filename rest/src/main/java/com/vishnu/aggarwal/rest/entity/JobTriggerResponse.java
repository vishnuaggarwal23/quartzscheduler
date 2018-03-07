package com.vishnu.aggarwal.rest.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The type Job trigger response.
 */
@Entity
@Table(name = "QRTZ_TRIGGERS_RESPONSE")
@Getter
@Setter
@ToString
public class JobTriggerResponse extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String triggerKeyName;
    private String triggerGroupName;
    private String jobKeyName;
    private String jobGroupName;
    private Integer responseCode;
    @Column(length = Integer.MAX_VALUE, columnDefinition = "LONGTEXT")
    private String responseHeader;
    @Column(length = Integer.MAX_VALUE, columnDefinition = "LONGTEXT")
    private String responseBody;
    private Date fireTime;

}
