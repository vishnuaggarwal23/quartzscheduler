package com.vishnu.aggarwal.rest.interfaces;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
Created by vishnu on 20/4/18 11:27 AM
*/
@Transactional
public interface TokenAuthenticationService {

    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationManager authenticationManager) throws AuthenticationException;

    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;

    Boolean removeAuthentication(Authentication authentication, HttpServletRequest request) throws AuthenticationException;
}
