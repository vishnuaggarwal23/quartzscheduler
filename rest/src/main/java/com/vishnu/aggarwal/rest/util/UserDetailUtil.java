package com.vishnu.aggarwal.rest.util;

/*
Created by vishnu on 6/3/18 10:32 AM
*/

import com.vishnu.aggarwal.core.BaseMessageResolver;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@CommonsLog
public class UserDetailUtil implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    BaseMessageResolver baseMessageResolver;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.findByUsername(username);
            if (isNull(user)) {
                throw new UsernameNotFoundException(baseMessageResolver.getMessage("username.not.found"));
            }
            return user;
        } catch (HibernateException e) {
            throw new UsernameNotFoundException(baseMessageResolver.getMessage("multiple.usernames.found"));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage(), e);
        }
    }
}
