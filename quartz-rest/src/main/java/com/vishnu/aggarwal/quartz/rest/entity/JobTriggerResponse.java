package com.vishnu.aggarwal.quartz.rest.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.FetchType.EAGER;

/**
 * The type Job trigger response.
 */
@Entity
@Table(name = "QRTZ_TRIGGERS_RESPONSE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JobTriggerResponse extends BaseEntity<Long> implements Serializable {

    private static final long serialVersionUID = -2248190721476487645L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String triggerKey;

    @NotNull
    @ManyToOne(fetch = EAGER)
    private User triggerGroup;

    @NotNull
    @NotBlank
    @NotEmpty
    private String jobKey;

    @NotNull
    @ManyToOne(fetch = EAGER)
    private User jobGroup;

    @NotNull
    private int responseCode;

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

    public JobTriggerResponse(String triggerKey, User triggerGroup, String jobKey, User jobGroup, int responseCode, String responseHeader, String responseBody, Date fireTime) {
        super();
        this.triggerKey = triggerKey;
        this.triggerGroup = triggerGroup;
        this.jobKey = jobKey;
        this.jobGroup = jobGroup;
        this.responseCode = responseCode;
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
        this.fireTime = fireTime;
    }

}
