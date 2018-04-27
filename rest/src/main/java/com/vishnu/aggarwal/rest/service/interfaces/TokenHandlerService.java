package com.vishnu.aggarwal.rest.service.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public interface TokenHandlerService {
    public User parseToken(String xAuthToken) throws AuthenticationException;

    public Map<String, Object> generateToken(User user);

    public Boolean isTokenExpired(String token);

    public Boolean isValidToken(String token);
}