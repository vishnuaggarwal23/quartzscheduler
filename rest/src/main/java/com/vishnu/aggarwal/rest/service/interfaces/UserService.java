package com.vishnu.aggarwal.rest.service.interfaces;

/*
Created by vishnu on 20/4/18 11:18 AM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.hibernate.HibernateException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User getCurrentLoggedInUser();
    public User findByUsername(String username) throws HibernateException;
}
