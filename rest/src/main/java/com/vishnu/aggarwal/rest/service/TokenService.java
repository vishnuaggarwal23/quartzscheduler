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

@Service
@CommonsLog
public class TokenService extends BaseService {
    @Autowired
    TokenRepoService tokenRepoService;

    public Token save(Token token) {
        return tokenRepoService.save(token);
    }

    public Token findByTokenAndIsDeleted(String token, Boolean isDeleted) {
        return tokenRepoService.findByTokenAndIsDeleted(token, isDeleted);
    }
}
