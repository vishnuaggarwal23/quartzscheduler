package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 6/3/18 10:33 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.repository.RoleRepoService;
import com.vishnu.aggarwal.rest.service.repository.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Service
@CommonsLog
public class UserService extends BaseService {

    @Autowired
    UserRepoService userRepoService;

    @Autowired
    RoleRepoService roleRepoService;

    public User getCurrentLoggedInUser() {
        return (User) getContext().getAuthentication().getPrincipal();
    }

    public User findByUsername(String username) throws HibernateException {
        return userRepoService.findByUsername(username);
    }
}
