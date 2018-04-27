package com.vishnu.aggarwal.rest.service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

/*
Created by vishnu on 20/4/18 12:50 PM
*/

/**
 * The type User token dao service.
 */
@Service
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
