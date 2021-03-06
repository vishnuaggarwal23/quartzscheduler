package com.vishnu.aggarwal.quartz.rest.service;

/*
Created by vishnu on 6/3/18 10:33 AM
*/

import com.vishnu.aggarwal.quartz.core.service.BaseService;
import com.vishnu.aggarwal.quartz.rest.interfaces.UserService;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.UserRepoService;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import lombok.extern.apachecommons.CommonsLog;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.vishnu.aggarwal.quartz.core.enums.Status.ACTIVE;
import static java.lang.Long.valueOf;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

/**
 * The type User service.
 */
@Service
@CommonsLog
@Transactional
public class UserServiceImpl extends BaseService implements UserService {

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
    public UserServiceImpl(UserRepoService userRepoService) {
        this.userRepoService = userRepoService;
    }

    /**
     * Gets current logged in user.
     *
     * @return the current logged in user
     */
    public User getCurrentLoggedInUser() throws HibernateException {
        return findByUsername(((String) getContext().getAuthentication().getPrincipal()).trim());
    }

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     * @throws HibernateException the hibernate exception
     */
    public User findByUsername(final String username) throws HibernateException {
        return userRepoService.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = findByUsername(username);
            user.setUserAuthorities(user.getUserAuthorities().stream().filter(userAuthority -> nonNull(userAuthority) && (userAuthority.getStatus() == ACTIVE)).collect(toList()));
            return user;
        } catch (HibernateException e) {
            throw new UsernameNotFoundException(getMessage("multiple.usernames.found"));
        } catch (UsernameNotFoundException | NullPointerException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    public User findById(final Long id) {
        return userRepoService.findById(id);
    }

    public User findById(final String id) {
        return userRepoService.findById(valueOf(id));
    }
}
