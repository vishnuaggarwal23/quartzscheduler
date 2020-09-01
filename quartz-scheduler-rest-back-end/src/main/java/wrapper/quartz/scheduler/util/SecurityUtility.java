package wrapper.quartz.scheduler.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wrapper.quartz.scheduler.dto.SecurityAuthenticationToken;

/**
 * The type Security utility.
 */
public final class SecurityUtility {
    /**
     * Gets authentication.
     *
     * @return the authentication
     * @throws AccessDeniedException                      the access denied exception
     * @throws AuthenticationCredentialsNotFoundException the authentication credentials not found exception
     */
    public static SecurityAuthenticationToken getAuthentication() throws AccessDeniedException, AuthenticationCredentialsNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authentication found in Security Context");
        }
        if (CollectionUtils.isEmpty(authentication.getAuthorities())) {
            throw new AccessDeniedException("User has no authorities assigned.");
        }
        return (SecurityAuthenticationToken) authentication;
    }

    /**
     * Gets principal.
     *
     * @return the principal
     * @throws UsernameNotFoundException                  the username not found exception
     * @throws AccessDeniedException                      the access denied exception
     * @throws AuthenticationCredentialsNotFoundException the authentication credentials not found exception
     */
    public static String getPrincipal() throws UsernameNotFoundException, AccessDeniedException, AuthenticationCredentialsNotFoundException {
        String principal = getAuthentication().getPrincipal();
        if (principal == null) {
            throw new UsernameNotFoundException("No principal found in Security Context");
        }
        return principal;
    }
}
