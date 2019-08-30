package com.vishnu.aggarwal.rest.service.dao.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 20/4/18 12:50 PM
*/

@Service
@Transactional
public class UserTokenDAOService extends JdbcDaoSupport {
    @Autowired
    public UserTokenDAOService(JdbcTemplate jdbcTemplate) {
        super();
        super.setJdbcTemplate(jdbcTemplate);
    }

}
