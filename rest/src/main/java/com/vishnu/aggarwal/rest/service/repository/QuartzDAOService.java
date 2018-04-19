package com.vishnu.aggarwal.rest.service.repository;

/*
Created by vishnu on 18/4/18 11:33 AM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class QuartzDAOService extends JdbcDaoSupport {

    @Autowired
    public QuartzDAOService(JdbcTemplate jdbcTemplate) {
        super();
        super.setJdbcTemplate(jdbcTemplate);
    }

    public Boolean isUniqueTriggerKey(String triggerKeyName, String groupName) {
        return getJdbcTemplate().queryForObject(
                "SELECT count(QRTZ_TRIGGERS.TRIGGER_NAME) FROM QRTZ_TRIGGERS WHERE QRTZ_TRIGGERS.TRIGGER_NAME = ? AND QRTZ_TRIGGERS.TRIGGER_GROUP = ?"
                , new Object[]{triggerKeyName, groupName}
                , Long.class) == 0L;
    }

    public Boolean isUniqueJobKey(String jobKeyName, String groupName) {
        return getJdbcTemplate().queryForObject(
                "SELECT count(QRTZ_JOB_DETAILS.JOB_NAME) FROM QRTZ_JOB_DETAILS WHERE QRTZ_JOB_DETAILS.JOB_NAME = ? AND QRTZ_JOB_DETAILS.JOB_GROUP = ?"
                , new Object[]{jobKeyName, groupName}
                , Long.class) == 0L;
    }
}
