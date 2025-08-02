package com.digitalwallet.walletservice.exception;

/**
 * Exception thrown when a requested wallet cannot be found in the system.

 * This typically occurs when a wallet ID or related resource does not exist
 * for the given customer or user context.

 * Usually mapped to HTTP 404 Not Found in REST APIs.
 */
public class WalletNotFoundException extends RuntimeException {

    /**
     * Constructs a new WalletNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the wallet was not found
     */
    public WalletNotFoundException(String message) {
        super(message);
    }
}