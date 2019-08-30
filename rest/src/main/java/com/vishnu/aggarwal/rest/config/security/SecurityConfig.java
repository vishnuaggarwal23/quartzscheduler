package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 19/4/18 4:08 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vishnu.aggarwal.core.config.BaseMessageResolver;
import com.vishnu.aggarwal.rest.interfaces.TokenAuthenticationService;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final BaseMessageResolver baseMessageResolver;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final LogoutHandler logoutHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final Gson gson;

    @Autowired
    public SecurityConfig(
            ObjectMapper objectMapper,
            TokenAuthenticationService tokenAuthenticationService,
            BaseMessageResolver baseMessageResolver,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserService userService,
            com.vishnu.aggarwal.rest.config.security.LogoutSuccessHandler logoutSuccessHandler,
            com.vishnu.aggarwal.rest.config.security.LogoutHandler logoutHandler,
            com.vishnu.aggarwal.rest.config.security.AccessDeniedHandler accessDeniedHandler,
            Gson gson) {
        super();
        this.objectMapper = objectMapper;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.logoutHandler = logoutHandler;
        this.gson = gson;
        this.authenticationEntryPoint = new Http403ForbiddenEntryPoint();
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and()
                .formLogin().disable()
                .logout().disable()
                .servletApi()
                .and()
                .headers().xssProtection().xssProtectionEnabled(TRUE).and().cacheControl().and().contentTypeOptions().and().frameOptions().and().httpStrictTransportSecurity().disable()
                .and()
                .anonymous().disable()
                .addFilter(new LoginFilter(new AntPathRequestMatcher(USER_LOGIN_REGEX, POST.name()), tokenAuthenticationService, baseMessageResolver, authenticationManagerBean(), userService, gson))
                .addFilterBefore(new AuthenticationFilter(
                        tokenAuthenticationService,
                        baseMessageResolver,
                        userService,
                        new AntPathRequestMatcher(USER_LOGIN_REGEX), new AntPathRequestMatcher(USER_AUTHENTICATE_REGEX),
                        gson,
                        new AntPathRequestMatcher(ERROR_REGEX),
                        new AntPathRequestMatcher(ACTUATOR_REGEX),
                        new AntPathRequestMatcher(SYSTEM_MONITOR_REGEX)), LogoutFilter.class)
                .addFilterAt(new LogoutFilter(
                                USER_LOGOUT_REGEX,
                                logoutSuccessHandler,
                                new AntPathRequestMatcher(USER_LOGOUT_REGEX, POST.name()),
                                tokenAuthenticationService,
                                logoutHandler),
                        org.springframework.security.web.authentication.logout.LogoutFilter.class)
                .authorizeRequests()
                .antMatchers(OPTIONS).permitAll()
                .antMatchers(POST, USER_LOGIN_REGEX).permitAll()
                .antMatchers(QUARTZ_REGEX, VALIDATION_REGEX).hasAuthority(ROLE_USER)
                .antMatchers(USER_AUTHENTICATE_REGEX).permitAll()
                .antMatchers(POST, USER_LOGOUT_REGEX).authenticated()
                .antMatchers(ACTUATOR_REGEX).permitAll()
                .antMatchers(ERROR_REGEX).denyAll()
                .antMatchers(SYSTEM_MONITOR_REGEX).permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(SPRING_SECURITY_EXCLUDED_PATHS)
                .mvcMatchers(SPRING_SECURITY_EXCLUDED_PATHS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
