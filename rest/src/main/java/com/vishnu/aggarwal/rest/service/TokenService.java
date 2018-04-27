package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 21/4/18 2:36 PM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.service.repository.TokenRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Token service.
 */
@Service
@CommonsLog
public class TokenService extends BaseService {
    /**
     * The Token repo service.
     */
    @Autowired
    TokenRepoService tokenRepoService;

    /**
     * Save token.
     *
     * @param token the token
     * @return the token
     */
    public Token save(Token token) {
        return tokenRepoService.save(token);
    }

    /**
     * Find by token and is deleted token.
     *
     * @param token     the token
     * @param isDeleted the is deleted
     * @return the token
     */
    public Token findByTokenAndIsDeleted(String token, Boolean isDeleted) {
        return tokenRepoService.findByTokenAndIsDeleted(token, isDeleted);
    }
}
