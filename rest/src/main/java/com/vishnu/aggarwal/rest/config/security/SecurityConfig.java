package com.vishnu.aggarwal.rest.config.security;

/*
Created by vishnu on 19/4/18 4:08 PM
*/

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_ADMIN;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * The type Security config.
 */
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

    /**
     * Instantiates a new Security config.
     *
     * @param objectMapper               the object mapper
     * @param tokenAuthenticationService the token authentication service
     * @param baseMessageResolver        the base message resolver
     * @param bCryptPasswordEncoder      the b crypt password encoder
     * @param userService                the user service
     * @param logoutSuccessHandler       the logout success handler
     * @param logoutHandler              the logout handler
     * @param accessDeniedHandler        the access denied handler
     */
    @Autowired
    public SecurityConfig(ObjectMapper objectMapper, TokenAuthenticationService tokenAuthenticationService, BaseMessageResolver baseMessageResolver, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, com.vishnu.aggarwal.rest.config.security.LogoutSuccessHandler logoutSuccessHandler, com.vishnu.aggarwal.rest.config.security.LogoutHandler logoutHandler, com.vishnu.aggarwal.rest.config.security.AccessDeniedHandler accessDeniedHandler) {
        super();
        this.objectMapper = objectMapper;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.baseMessageResolver = baseMessageResolver;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.logoutHandler = logoutHandler;
        this.authenticationEntryPoint = new Http403ForbiddenEntryPoint();
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                    .antMatchers(OPTIONS).permitAll()
                    .antMatchers(POST, "/user/login").permitAll()
                    .antMatchers("/quartz/**/", "/validation/**/").hasAnyAuthority(ROLE_ADMIN, ROLE_USER)
                    .antMatchers("/**/user/authenticate").authenticated()
                    .antMatchers(POST, "/**/user/logout").authenticated()
                    .antMatchers("/error").denyAll()
                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
                .and()
                .formLogin().disable()
                .logout().disable()
                .servletApi()
                .and()
                .headers().xssProtection().xssProtectionEnabled(TRUE).and().cacheControl().and()
                .and()
                .addFilterBefore(new LoginFilter(new AntPathRequestMatcher("/user/login", POST.name()), tokenAuthenticationService, baseMessageResolver, objectMapper, authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AuthenticationFilter(tokenAuthenticationService, objectMapper, baseMessageResolver, authenticationManagerBean()), LogoutFilter.class)
                .addFilterAt(new LogoutFilter("/user/logout", logoutSuccessHandler, new AntPathRequestMatcher("/user/logout", POST.name()), tokenAuthenticationService, logoutHandler), org.springframework.security.web.authentication.logout.LogoutFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/actuator/**/", "/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/")
                .mvcMatchers("/actuator/**/", "/**/js/**/", "/**/css/**/", "/**/img/**/", "/**/font/**/", "/**/fonts/**/", "/**/webjars/**/", "/**/webjar/**/");
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
