package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 11:18 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface User service.
 */
@Transactional
public interface UserService extends UserDetailsService {
    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    User getCurrentLoggedInUser();

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    User findByUsername(final String username) throws HibernateException;

    User findById(final Long id);

    User findById(final String id);
}
