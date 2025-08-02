package com.digitalwallet.walletservice.exception;

/**
 * Exception thrown when a user tries to perform an action
 * with a role that is not authorized to do so.

 * Typically used in role-based access control scenarios where
 * a CUSTOMER or EMPLOYEE is restricted from accessing certain functionality.

 * Usually mapped to HTTP 403 Forbidden in REST APIs.
 */
public class InvalidRoleException extends RuntimeException {

    /**
     * Constructs a new InvalidRoleException with the specified detail message.
     *
     * @param message the detail message explaining the role violation
     */
    public InvalidRoleException(String message) {
        super(message);
    }
}