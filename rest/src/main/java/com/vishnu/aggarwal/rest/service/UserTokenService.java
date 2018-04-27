package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 20/4/18 12:14 PM
*/

import com.vishnu.aggarwal.core.enums.Status;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.service.repository.UserTokenRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTokenService extends BaseService {

    @Autowired
    UserTokenRepoService userTokenRepoService;

    public UserToken save(UserToken userToken) {
        return userTokenRepoService.save(userToken);
    }

    public Boolean updateTokenStatus(User user){
        return userTokenRepoService.inactivatePreviousUserTokens(user);
    }

    public UserToken findByToken(String token) {
        return userTokenRepoService.findByToken(token);
    }

    public List<UserToken> findAllUserTokens(User user){
        return userTokenRepoService.findAllUserTokens(user);
    }

    public UserToken findByUserAndStatus(User user, Status status){
        return userTokenRepoService.findByUserAndStatus(user, status);
    }
}
