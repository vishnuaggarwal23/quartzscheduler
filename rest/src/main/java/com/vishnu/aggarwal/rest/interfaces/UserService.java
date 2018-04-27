package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 11:18 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {
    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    public User getCurrentLoggedInUser();

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    public User findByUsername(String username) throws HibernateException;
}
