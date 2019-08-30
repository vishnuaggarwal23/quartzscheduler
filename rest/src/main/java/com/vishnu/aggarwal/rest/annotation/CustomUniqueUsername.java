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

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = CustomUniqueUsernameValidator.class)
public @interface CustomUniqueUsername {
    String message() default "{username.not.unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}