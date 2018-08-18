package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 6/3/18 10:33 AM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.repository.jpa.UserRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.util.Assert.notNull;

/**
 * The type User service.
 */
@Service
@CommonsLog
public class UserService extends BaseService implements com.vishnu.aggarwal.rest.interfaces.UserService {

    /**
     * The User repo service.
     */
    private final UserRepoService userRepoService;

    /**
     * Instantiates a new User service.
     *
     * @param userRepoService the user repo service
     */
    @Autowired
    public UserService(UserRepoService userRepoService) {
        this.userRepoService = userRepoService;
    }

    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    public User getCurrentLoggedInUser() throws HibernateException {
        User user = null;
        Object principal = getContext().getAuthentication().getPrincipal();
        if (principal instanceof String) {
            user = findByUsername(((String) principal).trim());
        } else if (principal instanceof User) {
            user = (User) principal;
        }
        notNull(user, formatMessage(getMessage("")));

        return user;
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
