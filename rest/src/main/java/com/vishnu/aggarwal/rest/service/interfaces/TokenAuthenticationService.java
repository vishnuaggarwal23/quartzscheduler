package com.vishnu.aggarwal.rest.service.interfaces;

import com.vishnu.aggarwal.rest.entity.UserToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Created by vishnu on 20/4/18 11:27 AM
*/
public interface TokenAuthenticationService {
    public Authentication getAuthenticationForLogin(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException, IOException;

    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response);

    public UserToken addAuthentication(HttpServletResponse response, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken);

    Boolean removeAuthentication(Authentication authentication);
}
