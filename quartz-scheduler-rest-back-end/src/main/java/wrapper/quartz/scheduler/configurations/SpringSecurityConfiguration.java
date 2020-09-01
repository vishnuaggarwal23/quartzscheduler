package wrapper.quartz.scheduler.configurations;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import wrapper.quartz.scheduler.constants.ApplicationConstants;
import wrapper.quartz.scheduler.enums.AuthorityEnum;
import wrapper.quartz.scheduler.filters.LoginFilter;
import wrapper.quartz.scheduler.filters.ValidationFilter;
import wrapper.quartz.scheduler.service.JwtService;
import wrapper.quartz.scheduler.service.UserService;
import wrapper.quartz.scheduler.util.LoggerUtility;
import wrapper.quartz.scheduler.util.TypeTokenUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableGlobalAuthentication
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationEventPublisher authenticationEventPublisher;

    public SpringSecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder,
                                       UserService userService,
                                       Gson gson,
                                       JwtService jwtService,
                                       AuthenticationEventPublisher authenticationEventPublisher) {
        super();
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationEventPublisher = authenticationEventPublisher;
        this.authenticationSuccessHandler = new AuthenticationSuccessHandler(gson);
        this.authenticationFailureHandler = new AuthenticationFailureHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String[] urlPatterns = {
                "/**/js/**/",
                "/**/css/**/",
                "/**/img/**/",
                "/**/font/**/",
                "/**/fonts/**/",
                "/**/webjars/**/",
                "/**/webjar/**/",
                "/**/system/monitor/**/",
                "/**/actuator/**/"
        };
        web.ignoring().antMatchers(urlPatterns).mvcMatchers(urlPatterns);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().flush();
                    response.getWriter().close();
                    response.flushBuffer();
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().flush();
                    response.getWriter().close();
                    response.flushBuffer();
                })
                .and()
                .formLogin()
                .disable()
                .logout()
                .disable()
                .headers()
                .xssProtection()
                .xssProtectionEnabled(true).block(true)
                .and()
                .cacheControl()
                .and()
                .contentTypeOptions()
                .and()
                .frameOptions()
                .and()
                .httpStrictTransportSecurity()
                .and()
                .and()
                .anonymous()
                .disable()
                .addFilter(new LoginFilter(
                        new AntPathRequestMatcher("/api/v1/login", HttpMethod.POST.name()),
                        authenticationManagerBean(),
                        userService,
                        jwtService,
                        authenticationSuccessHandler,
                        authenticationFailureHandler))
                .addFilterBefore(new ValidationFilter(
                        userService,
                        new AntPathRequestMatcher("/api/v1/login"),
                        new AntPathRequestMatcher("/api/v1/validation"),
                        jwtService,
                        authenticationSuccessHandler,
                        authenticationEventPublisher), LogoutFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers("/api/v1/quartz/job/**")
                .hasAnyRole(AuthorityEnum.ROLE_JOB.name())
                .antMatchers("/api/v1/quartz/trigger/**")
                .hasAnyRole(AuthorityEnum.ROLE_TRIGGER.name())
                .antMatchers("/api/v1/user/**")
                .hasAnyRole(AuthorityEnum.ROLE_APPLICATION_USER.name(), AuthorityEnum.ROLE_GROUP_ADMIN.name(), AuthorityEnum.ROLE_APPLICATION_ADMIN.name())
                .antMatchers("/api/v1/group/**")
                .hasAnyRole(AuthorityEnum.ROLE_GROUP_ADMIN.name(), AuthorityEnum.ROLE_GROUP_USER.name(), AuthorityEnum.ROLE_APPLICATION_ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .servletApi();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

@Slf4j
class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {

    private final Gson gson;

    AuthenticationSuccessHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> tokenResponse = gson.fromJson(authentication.getDetails().toString(), TypeTokenUtility.mapOfStringAndMapOfStringAndObject());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.AUTHORIZATION, MapUtils.getString(tokenResponse, "token"));
        response.addHeader(ApplicationConstants.SecurityConstants.X_AUTH_TOKEN, MapUtils.getString(tokenResponse, "token"));
        response.getWriter().flush();
        response.getWriter().close();
        response.flushBuffer();
    }
}

@Slf4j
class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        LoggerUtility.error(log, exception);
        response.getWriter().flush();
        response.getWriter().close();
        response.flushBuffer();
    }
}