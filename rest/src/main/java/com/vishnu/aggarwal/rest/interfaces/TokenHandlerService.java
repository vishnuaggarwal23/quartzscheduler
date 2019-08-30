package com.vishnu.aggarwal.rest.interfaces;

/*
Created by vishnu on 20/4/18 12:11 PM
*/

import com.vishnu.aggarwal.rest.entity.User;
import io.jsonwebtoken.JwtException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
public interface TokenHandlerService {
    User parseToken(final String token) throws JwtException;

    Map<String, Object> generateToken(final User user);

    Boolean isValidToken(final String token) throws JwtException;
}