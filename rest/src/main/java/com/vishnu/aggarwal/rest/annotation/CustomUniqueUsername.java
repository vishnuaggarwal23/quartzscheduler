package com.vishnu.aggarwal.rest.annotation;

/*
Created by vishnu on 19/8/18 1:42 PM
*/

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Custom unique username.
 */
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = CustomUniqueUsernameValidator.class)
public @interface CustomUniqueUsername {
    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{username.not.unique}";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};
}