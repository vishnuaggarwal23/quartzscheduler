package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 11:18 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService extends UserDetailsService {
    User getCurrentLoggedInUser();

    User findByUsername(final String username) throws HibernateException;

    User findById(final Long id);

    User findById(final String id);

    Long getCurrentLoggedInUserId();
}
