package com.digitalwallet.walletservice.exception;

/**
 * Exception thrown when a wallet already exists for a customer
 * with the same currency or unique constraint.

 * This is typically used to prevent duplicate wallet creation attempts.
 * Usually mapped to HTTP 400 Bad Request in REST APIs.
 */
public class WalletAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new WalletAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining why the wallet already exists
     */
    public WalletAlreadyExistsException(String message) {
        super(message);
    }
}