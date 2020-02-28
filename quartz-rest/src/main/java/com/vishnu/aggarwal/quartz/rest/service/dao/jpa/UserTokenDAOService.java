package com.vishnu.aggarwal.quartz.rest.service.dao.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 20/4/18 12:50 PM
*/

/**
 * The type User token dao service.
 */
@Service
@Transactional
public class UserTokenDAOService extends JdbcDaoSupport {
    /**
     * Instantiates a new User token dao service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public UserTokenDAOService(JdbcTemplate jdbcTemplate) {
        super();
        super.setJdbcTemplate(jdbcTemplate);
    }

}
