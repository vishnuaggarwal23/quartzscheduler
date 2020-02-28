package com.vishnu.aggarwal.quartz.rest.service;

/*
Created by vishnu on 21/4/18 2:36 PM
*/

import com.vishnu.aggarwal.quartz.core.service.BaseService;
import com.vishnu.aggarwal.quartz.rest.entity.Token;
import com.vishnu.aggarwal.quartz.rest.service.repository.jpa.TokenRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Token service.
 */
@Service
@CommonsLog
@Transactional
public class TokenService extends BaseService {
    /**
     * The Token repo service.
     */
    private final TokenRepoService tokenRepoService;

    /**
     * Instantiates a new Token service.
     *
     * @param tokenRepoService the token repo service
     */
    @Autowired
    public TokenService(TokenRepoService tokenRepoService) {
        this.tokenRepoService = tokenRepoService;
    }

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
     * @param token the token
     * @return the token
     */
    public Token findByToken(String token) {
        return tokenRepoService.findByToken(token);
    }
}
