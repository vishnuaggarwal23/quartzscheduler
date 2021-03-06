package com.vishnu.aggarwal.quartz.rest.service;

/*
Created by vishnu on 20/4/18 12:14 PM
*/

import com.vishnu.aggarwal.quartz.core.enums.Status;
import com.vishnu.aggarwal.quartz.core.service.BaseService;
import com.vishnu.aggarwal.quartz.rest.entity.UserToken;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.UserTokenRepoService;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type User token service.
 */
@Service
@Transactional
public class UserTokenService extends BaseService {

    /**
     * The User token repo service.
     */
    private final UserTokenRepoService userTokenRepoService;

    @Autowired
    public UserTokenService(UserTokenRepoService userTokenRepoService) {
        this.userTokenRepoService = userTokenRepoService;
    }

    /**
     * Save user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UserToken save(UserToken userToken) {
        return userTokenRepoService.save(userToken);
    }

    /**
     * Update token status boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public Boolean updateTokenStatus(User user) {
        return userTokenRepoService.inactivatePreviousUserTokens(user);
    }

    /**
     * Find by token user token.
     *
     * @param token the token
     * @return the user token
     */
    public UserToken findByToken(String token) {
        return userTokenRepoService.findByToken(token);
    }

    /**
     * Find all user tokens list.
     *
     * @param user the user
     * @return the list
     */
    public List<UserToken> findAllUserTokens(User user) {
        return userTokenRepoService.findAllUserTokens(user);
    }

    /**
     * Find by user and status user token.
     *
     * @param user   the user
     * @param status the status
     * @return the user token
     */
    public UserToken findByUserAndStatus(User user, Status status) {
        return userTokenRepoService.findByUserAndStatus(user, status);
    }
}
