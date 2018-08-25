package com.vishnu.aggarwal.rest.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

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

    @NotNull
    @NotBlank
    @NotEmpty
    private String triggerKeyName;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private User triggerGroupName;

    @NotNull
    @NotBlank
    @NotEmpty
    private String jobKeyName;

    @NotNull
    @ManyToOne(fetch = LAZY)
    private User jobGroupName;

    @NotNull
    private Integer responseCode;

    @NotNull
    @Column(length = Integer.MAX_VALUE, columnDefinition = "LONGTEXT")
    private String responseHeader;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(length = Integer.MAX_VALUE, columnDefinition = "LONGTEXT")
    private String responseBody;

    @NotNull
    private Date fireTime;

}
