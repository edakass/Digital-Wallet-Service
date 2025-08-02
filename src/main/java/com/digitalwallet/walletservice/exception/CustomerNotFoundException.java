package com.digitalwallet.walletservice.exception;

/**
 * Exception thrown when a requested customer cannot be found in the system.

 * Typically mapped to HTTP 404 Not Found in REST APIs.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Constructs a new CustomerNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the customer was not found
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
