package com.digitalwallet.walletservice.enums;

/**
 * Enum representing the status of a financial transaction.

 * This status is used to track the state of deposit or withdraw operations.
 */
public enum TransactionStatus {

    /**
     * Transaction is waiting for approval (e.g., amount > threshold).
     */
    PENDING,

    /**
     * Transaction has been approved and processed.
     */
    APPROVED,

    /**
     * Transaction has been reviewed and rejected.
     */
    DENIED
}