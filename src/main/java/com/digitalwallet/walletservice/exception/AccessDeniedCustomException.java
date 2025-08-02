package com.digitalwallet.walletservice.exception;

/**
 * Custom exception thrown when a user tries to perform an action
 * they do not have permission for (access denied).

 * Typically mapped to HTTP 403 Forbidden in REST APIs.
 */
public class AccessDeniedCustomException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedCustomException with the specified detail message.
     *
     * @param message the detail message explaining the access restriction
     */
    public AccessDeniedCustomException(String message) {
        super(message);
    }
}
