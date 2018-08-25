package com.vishnu.aggarwal.core.exceptions;

/*
Created by vishnu on 18/8/18 10:56 PM
*/

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Invalid request exception.
 */
@Getter
@Setter
@ToString
public class InvalidRequestException extends RuntimeException {
    /**
     * Instantiates a new Invalid request exception.
     */
    public InvalidRequestException() {
        super();
    }

    /**
     * Instantiates a new Invalid request exception.
     *
     * @param message the message
     */
    public InvalidRequestException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Invalid request exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Invalid request exception.
     *
     * @param cause the cause
     */
    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Invalid request exception.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    protected InvalidRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }
}
