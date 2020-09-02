package wrapper.quartz.scheduler.configurations;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import wrapper.quartz.scheduler.dto.SecurityAuthenticationToken;
import wrapper.quartz.scheduler.entity.jpa.User;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableGlobalAuthentication
@Slf4j
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
        this.authenticationSuccessHandler = new AuthenticationSuccessHandler(gson, userService);
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
                "/**/actuator/**/",
                "/**/swagger-resources/**",
                "/**/swagger-ui/**",
                "/**/v2/api-docs/**",
                "/error"
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
                    LoggerUtility.error(log, authException);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().flush();
                    response.getWriter().close();
                    response.flushBuffer();
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    LoggerUtility.error(log, accessDeniedException);
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
                        new AntPathRequestMatcher("/**/api/v1/login", HttpMethod.POST.name()),
                        authenticationManagerBean(),
                        userService,
                        jwtService,
                        authenticationSuccessHandler,
                        authenticationFailureHandler))
                .addFilterBefore(new ValidationFilter(
                        userService,
                        new AntPathRequestMatcher("/**/api/v1/login"),
                        new AntPathRequestMatcher("/**/api/v1/validation"),
                        jwtService,
                        authenticationSuccessHandler,
                        authenticationEventPublisher), LogoutFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers("/**/api/v1/quartz/job/**")
                .hasAnyRole(AuthorityEnum.ROLE_JOB.name())
                .antMatchers("/**/api/v1/quartz/trigger/**")
                .hasAnyRole(AuthorityEnum.ROLE_TRIGGER.name())
                .antMatchers("/**/api/v1/user/**")
                .hasAnyRole(AuthorityEnum.ROLE_APPLICATION_USER.name(), AuthorityEnum.ROLE_GROUP_ADMIN.name(), AuthorityEnum.ROLE_APPLICATION_ADMIN.name())
                .antMatchers("/**/api/v1/group/**")
                .hasAnyRole(AuthorityEnum.ROLE_GROUP_ADMIN.name(), AuthorityEnum.ROLE_GROUP_USER.name(), AuthorityEnum.ROLE_APPLICATION_ADMIN.name())
                .antMatchers("/error", "/**/swagger-ui/**", "/**/v2/api-docs/**", "/**/swagger-resources/**")
                .permitAll()
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
    private final UserService userService;

    AuthenticationSuccessHandler(Gson gson, UserService userService) {
        this.gson = gson;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityAuthenticationToken securityAuthenticationToken = (SecurityAuthenticationToken) authentication;
        User user = userService.loadUserByUsernameOrEmail(securityAuthenticationToken.getPrincipal(), securityAuthenticationToken.getPrincipal());
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", user.getId());
        responseBody.put("username", user.getUsername());
        responseBody.put("email", user.getEmail());
        Map<String, Object> groupBody = new HashMap<>();
        groupBody.put("id", user.getQuartzGroup().getId());
        groupBody.put("name", user.getQuartzGroup().getName());
        responseBody.put("group", groupBody);
        responseBody.put("authorities", user
                .getAuthorities()
                .stream()
                .filter(Objects::nonNull)
                .map(it -> {
                    Map<String, Object> authorityBody = new HashMap<>();
                    authorityBody.put("id", it.getId());
                    authorityBody.put("authority", it.getAuthority());
                    return authorityBody;
                })
                .collect(Collectors.toSet()));
        responseBody.put("accessToken", securityAuthenticationToken.getAccessToken());
        responseBody.put("refreshToken", securityAuthenticationToken.getRefreshToken());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(ApplicationConstants.SecurityConstants.ACCESS_TOKEN, securityAuthenticationToken.getAccessToken());
        response.addHeader(ApplicationConstants.SecurityConstants.REFRESH_TOKEN, securityAuthenticationToken.getRefreshToken());
        response.getWriter().write(gson.toJson(responseBody, TypeTokenUtility.mapOfStringAndObject()));
        response.getWriter().flush();
        response.getWriter().close();
        response.flushBuffer();
    }
}

@Slf4j
class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        LoggerUtility.error(log, exception);
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();
        response.getWriter().close();
        response.flushBuffer();
    }
}