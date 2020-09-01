package wrapper.quartz.scheduler.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import wrapper.quartz.scheduler.constants.ApplicationConstants;
import wrapper.quartz.scheduler.dto.SecurityAuthenticationToken;
import wrapper.quartz.scheduler.service.JwtService;
import wrapper.quartz.scheduler.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ValidationFilter extends GenericFilterBean {
    private final UserService userService;
    private final RequestMatcher loginRequestMatcher;
    private final RequestMatcher validationRequestMatcher;
    private final JwtService jwtService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationEventPublisher authenticationEventPublisher;

    public ValidationFilter(
            UserService userService,
            RequestMatcher loginRequestMatcher,
            RequestMatcher validationRequestMatcher,
            JwtService jwtService,
            AuthenticationSuccessHandler authenticationSuccessHandler,
            AuthenticationEventPublisher authenticationEventPublisher) {
        super();
        this.userService = userService;
        this.loginRequestMatcher = loginRequestMatcher;
        this.validationRequestMatcher = validationRequestMatcher;
        this.jwtService = jwtService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    private String getAuthenticationToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(token)) {
            token = httpServletRequest.getHeader(ApplicationConstants.SecurityConstants.X_AUTH_TOKEN);
        }
        if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        return token;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (loginRequestMatcher.matches(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String token = getAuthenticationToken(httpServletRequest);
        if (StringUtils.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }

        Jws<Claims> jws = jwtService.parseToken(token);
        UserDetails userDetails = userService.loadUserByUsername(jws.getBody().getSubject());
        SecurityAuthenticationToken securityAuthenticationToken = new SecurityAuthenticationToken(userDetails);
        securityAuthenticationToken.setAccessToken(token);
        SecurityContextHolder.getContext().setAuthentication(securityAuthenticationToken);
        authenticationEventPublisher.publishAuthenticationSuccess(securityAuthenticationToken);
        if (validationRequestMatcher.matches(httpServletRequest)) {
            authenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, (HttpServletResponse) response, securityAuthenticationToken);
        } else {
            chain.doFilter(request, response);
        }
    }
}