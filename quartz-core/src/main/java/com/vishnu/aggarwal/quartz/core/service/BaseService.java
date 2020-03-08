package com.vishnu.aggarwal.quartz.core.service;

/*
Created by vishnu on 5/3/18 10:37 AM
*/

import com.vishnu.aggarwal.quartz.core.config.BaseMessageResolver;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The type Base service.
 */
@CommonsLog
public abstract class BaseService {

    /**
     * The Base message resolver.
     */
    @Autowired
    protected BaseMessageResolver baseMessageResolver;

    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @return the message
     */
    @NonNull
    @NotEmpty
    @NotBlank
    protected final String getMessage(@NonNull @NotBlank @NotEmpty final String messageCode) {
        return baseMessageResolver.getMessage(messageCode);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    protected final String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage) {
        return baseMessageResolver.getMessage(messageCode, defaultMessage);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage, @NonNull @NotEmpty final Object... args) {
        return baseMessageResolver.getMessage(messageCode, args, defaultMessage);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode,@NonNull @NotEmpty final  Object... args) {
        return baseMessageResolver.getMessage(messageCode, args);
    }

    /**
     * Format message string.
     *
     * @param messagePattern   the message pattern
     * @param messageArguments the message arguments
     * @return the string
     */
    protected String formatMessage(String messagePattern, Object... messageArguments) {
        if (isNotBlank(messagePattern)) {
            if (allNotNull(messageArguments)) {
                return format(messagePattern, messageArguments);
            }
            return format(messagePattern);
        }
        return EMPTY;
    }
}
