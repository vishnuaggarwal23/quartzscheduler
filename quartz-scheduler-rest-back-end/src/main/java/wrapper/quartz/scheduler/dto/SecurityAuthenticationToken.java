package wrapper.quartz.scheduler.dto;

import io.jsonwebtoken.lang.Assert;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class SecurityAuthenticationToken extends AbstractAuthenticationToken {
    private String principal;
    private String credentials;
    private String accessToken;
    private String refreshToken;

    public SecurityAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String principal, String credentials) {
        super(authorities);
        super.setAuthenticated(true);
        this.principal = principal;
        this.credentials = credentials;
    }

    public SecurityAuthenticationToken(UserDetails userDetails) {
        this(userDetails.getAuthorities(), userDetails.getUsername(), userDetails.getPassword());
    }

    public void setAccessToken(String accessToken) {
        Assert.notNull(accessToken, "Access token cannot be null");
        Assert.hasText(accessToken, "Access token cannot be blank");
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        Assert.notNull(refreshToken, "Refresh token cannot be null");
        Assert.hasText(refreshToken, "Refresh token cannot be blank");
        this.refreshToken = refreshToken;
    }
}