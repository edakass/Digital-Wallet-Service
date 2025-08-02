package com.digitalwallet.walletservice.enums;

/**
 * Enum representing the type of the opposite party involved in a transaction.

 * This is used to distinguish the source or destination of funds in deposit or withdraw operations.
 */
public enum OppositePartyType {

    /**
     * Represents a transaction involving a bank IBAN.
     */
    IBAN,

    /**
     * Represents a transaction involving an internal payment ID or reference.
     */
    PAYMENT
}