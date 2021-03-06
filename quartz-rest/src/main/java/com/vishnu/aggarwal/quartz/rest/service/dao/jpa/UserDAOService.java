package com.vishnu.aggarwal.quartz.rest.service.dao.jpa;

/*
Created by vishnu on 19/8/18 1:48 PM
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
public class UserDAOService extends JdbcDaoSupport {

    @Autowired
    public UserDAOService(JdbcTemplate jdbcTemplate) {
        super();
        super.setJdbcTemplate(jdbcTemplate);
    }

    @Cacheable(value = "isUsernameUnique", key = "#username", unless = "#result == null")
    public Boolean isUsernameUnique(String username) {
        return getJdbcTemplate().queryForObject(
                "SELECT count(*) from USER where USER.username = ? and USER.isDeleted = ?",
                new Object[]{username, false},
                Long.class
        ) == 0L;
    }
}
