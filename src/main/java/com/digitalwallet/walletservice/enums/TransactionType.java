package com.digitalwallet.walletservice.enums;

/**
 * Enum representing the type of a financial transaction.

 * Indicates whether the transaction is a deposit (incoming funds)
 * or a withdrawal (outgoing funds) in the system.
 */
public enum TransactionType {

    /**
     * Deposit transaction - money added to a wallet.
     */
    DEPOSIT,

    /**
     * Withdraw transaction - money taken out of a wallet.
     */
    WITHDRAW
}