package wrapper.quartz.scheduler.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import wrapper.quartz.scheduler.dto.LoginDTO;
import wrapper.quartz.scheduler.dto.SecurityAuthenticationToken;
import wrapper.quartz.scheduler.entity.jpa.User;
import wrapper.quartz.scheduler.service.JwtService;
import wrapper.quartz.scheduler.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public LoginFilter(RequestMatcher requestMatcher,
                       AuthenticationManager authenticationManager,
                       UserService userService,
                       JwtService jwtService,
                       AuthenticationSuccessHandler authenticationSuccessHandler,
                       AuthenticationFailureHandler authenticationFailureHandler) {
        super.setRequiresAuthenticationRequestMatcher(requestMatcher);
        super.setAuthenticationManager(authenticationManager);
        super.setPostOnly(true);
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
        this.jwtService = jwtService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginDTO loginDTO;
        try {
            loginDTO = objectMapper.readValue(IOUtils.toString(request.getReader()), LoginDTO.class);
            if (!loginDTO.isValid()) {
                throw new BadCredentialsException("Invalid login request. Username and/or password is missing");
            }
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
        Authentication authentication = super.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            try {
                User user = userService.loadUserByUsernameOrEmail(loginDTO.getUsername(), loginDTO.getUsername());
                SecurityAuthenticationToken securityAuthenticationToken = new SecurityAuthenticationToken(user);
                securityAuthenticationToken.setAccessToken(jwtService.generateAccessToken(user));
                securityAuthenticationToken.setRefreshToken(jwtService.generateRefreshToken(user));
                return securityAuthenticationToken;
            } catch (JwtException | IllegalArgumentException e) {
                throw new InternalAuthenticationServiceException(e.getMessage(), e);
            }
        }
        throw new InternalAuthenticationServiceException("Unable to authenticate user");
    }
}