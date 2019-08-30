package com.vishnu.aggarwal.rest.service;

/*
Created by vishnu on 21/4/18 2:36 PM
*/

import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.service.repository.jpa.TokenRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CommonsLog
@Transactional
public class TokenService extends BaseService {
    private final TokenRepoService tokenRepoService;

    @Autowired
    public TokenService(TokenRepoService tokenRepoService) {
        this.tokenRepoService = tokenRepoService;
    }

    public Token save(Token token) {
        return tokenRepoService.save(token);
    }

    public Token findByToken(String token) {
        return tokenRepoService.findByToken(token);
    }
}
