package com.digitalwallet.walletservice.exception;

/**
 * Exception thrown when a required request parameter or field is missing.

 * This is typically used when essential input data is not provided
 * by the client during an API request (e.g., missing query parameter, body field, etc.).

 * Usually mapped to HTTP 400 Bad Request in REST APIs.
 */
public class MissingParameterException extends RuntimeException {

    /**
     * Constructs a new MissingParameterException with the specified detail message.
     *
     * @param message the detail message explaining which parameter is missing
     */
    public MissingParameterException(String message) {
        super(message);
    }
}