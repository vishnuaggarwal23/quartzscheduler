package com.vishnu.aggarwal.admin.utils;

/*
Created by vishnu on 27/8/19 12:17 PM
*/

import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.web.util.WebUtils.getCookie;

@NoArgsConstructor(access = PRIVATE)
public class CookieUtils {

    public static Cookie create(final String name, final String value, @Nullable final String path, @Nullable Boolean httpOnly, int maxAge, @Nullable String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(isEmpty(path) ? "/" : path);
        cookie.setHttpOnly(isNull(httpOnly) ? FALSE : httpOnly);
        cookie.setMaxAge(maxAge);
        if (isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        return cookie;
    }

    public static Cookie remove(final HttpServletRequest httpServletRequest, final String name, @Nullable final String path, @Nullable final String domain, @Nullable Boolean httpOnly) {
        Cookie cookie = Optional.ofNullable(getCookie(httpServletRequest, name)).orElse(create(name, null, path, httpOnly, -1, domain));
        cookie.setValue(null);
        cookie.setMaxAge(-1);
        cookie.setPath(isEmpty(path) ? "/" : path);
        cookie.setHttpOnly(isNull(httpOnly) ? FALSE : httpOnly);
        if (isNotEmpty(domain)) {
            cookie.setDomain(domain);
        }
        return cookie;
    }
}
