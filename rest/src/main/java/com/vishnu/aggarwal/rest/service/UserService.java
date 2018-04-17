package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 6/3/18 10:33 AM
*/

import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.repository.RoleRepoService;
import com.vishnu.aggarwal.rest.service.repository.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type User service.
 */
@Service
@CommonsLog
public class UserService extends BaseService {

    /**
     * The User repo service.
     */
    @Autowired
    UserRepoService userRepoService;

    /**
     * The Role repo service.
     */
    @Autowired
    RoleRepoService roleRepoService;

    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    public User getCurrentLoggedInUser() {
        return (User) getContext().getAuthentication().getPrincipal();
    }

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    public User findByUsername(String username) throws HibernateException {
        return userRepoService.findByUsername(username);
    }

    public Boolean authenticateUser(String xAuthToken) {
        return TRUE;
    }

    public String login(UserDTO login) {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
