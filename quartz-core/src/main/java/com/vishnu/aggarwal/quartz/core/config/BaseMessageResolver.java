package com.vishnu.aggarwal.quartz.core.config;

import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * The interface Base message resolver.
 */
/*
Created by vishnu on 1/3/18 2:44 PM
*/
public interface BaseMessageResolver {
    /**
     * Gets message.
     *
     * @param messageCode the message code
     * @return the message
     */
    @NonNull @NotEmpty @NotBlank String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode);

    @NonNull @NotEmpty @NotBlank String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage);

    @NonNull @NotEmpty @NotBlank String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty final Object... args);

    @NonNull @NotEmpty @NotBlank String getMessage(@NonNull @NotEmpty @NotBlank final String messageCode, @NonNull @NotEmpty @NotBlank final String defaultMessage, @NonNull @NotEmpty final Object... args);
}
