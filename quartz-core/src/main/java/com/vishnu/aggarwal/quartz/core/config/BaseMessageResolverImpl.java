package com.vishnu.aggarwal.quartz.core.config;

/*
Created by vishnu on 1/3/18 2:42 PM
*/

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import static java.util.Locale.ROOT;

/**
 * The type Base message resolver.
 */
@Component
public class BaseMessageResolverImpl implements BaseMessageResolver {

    private final MessageSource messageSource;

    /**
     * Instantiates a new Base message resolver.
     *
     * @param messageSource the message source
     */
    @Autowired
    public BaseMessageResolverImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @NonNull
    @NotEmpty
    @NotBlank
    @Override
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode) {
        return messageSource.getMessage(messageCode, null, messageCode, ROOT);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    @Override
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage) {
        return messageSource.getMessage(messageCode, null, defaultMessage, ROOT);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    @Override
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage, @NonNull @NotEmpty final Object... args) {
        return messageSource.getMessage(messageCode, args, defaultMessage, ROOT);
    }

    @NonNull
    @NotEmpty
    @NotBlank
    @Override
    final public String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode,@NonNull @NotEmpty final  Object... args) {
        return messageSource.getMessage(messageCode, args, messageCode, ROOT);
    }
}
