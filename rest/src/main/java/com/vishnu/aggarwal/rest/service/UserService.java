package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 6/3/18 10:33 AM
*/

import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.repository.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type User service.
 */
@Service
@CommonsLog
public class UserService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.UserService {

    /**
     * The User repo service.
     */
    @Autowired
    UserRepoService userRepoService;

    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    public User getCurrentLoggedInUser() {
        return userRepoService.findByUsername((String) getContext().getAuthentication().getPrincipal());
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

    /**
     * Authenticate user boolean.
     *
     * @param xAuthToken the x auth token
     * @return the boolean
     */
    public Boolean authenticateUser(String xAuthToken) {
        return TRUE;
    }

    /**
     * Login string.
     *
     * @param login the login
     * @return the string
     */
    public String login(UserDTO login) {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepoService.findByUsername(username);
            if (isNull(user)) {
                throw new UsernameNotFoundException(getMessage("username.not.found"));
            }
            user.setUserAuthorities(user.getUserAuthorities());
            return user;
        } catch (HibernateException e) {
            throw new UsernameNotFoundException(getMessage("multiple.usernames.found"));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
