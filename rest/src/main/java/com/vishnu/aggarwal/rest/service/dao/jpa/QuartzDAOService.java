package com.vishnu.aggarwal.rest.service.dao.jpa;

/*
Created by vishnu on 18/4/18 11:33 AM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@Transactional
public class QuartzDAOService extends JdbcDaoSupport {

    @Autowired
    public QuartzDAOService(JdbcTemplate jdbcTemplate) {
        super();
        super.setJdbcTemplate(jdbcTemplate);
    }

    @Cacheable(value = "isUniqueTriggerKey", key = "#keyName + #groupName", unless = "#result == null")
    public Boolean isUniqueTriggerKey(String keyName, String groupName) {
        return getJdbcTemplate().queryForObject(
                "SELECT count(QRTZ_TRIGGERS.TRIGGER_NAME) FROM QRTZ_TRIGGERS WHERE QRTZ_TRIGGERS.TRIGGER_NAME = ? AND QRTZ_TRIGGERS.TRIGGER_GROUP = ?"
                , new Object[]{keyName, groupName}
                , Long.class) == 0L;
    }

    @Cacheable(value = "isUniqueJobKey", key = "#keyName + #groupName", unless = "#result == null")
    public Boolean isUniqueJobKey(String keyName, String groupName) {
        return getJdbcTemplate().queryForObject(
                "SELECT count(QRTZ_JOB_DETAILS.JOB_NAME) FROM QRTZ_JOB_DETAILS WHERE QRTZ_JOB_DETAILS.JOB_NAME = ? AND QRTZ_JOB_DETAILS.JOB_GROUP = ?"
                , new Object[]{keyName, groupName}
                , Long.class) == 0L;
    }
}
