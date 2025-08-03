package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.TransactionStatus;
import jakarta.validation.constraints.NotNull;

/**
 * DTO class for representing a transaction approval request.
 * This is used by EMPLOYEE roles to approve or reject a transaction.
 */
public class TransactionApprovalRequest {

    /**
     * ID of the transaction that is to be approved or rejected.
     */
    @NotNull
    private Long transactionId;

    /**
     * New status to set for the transaction (e.g. APPROVED or REJECTED).
     */
    @NotNull
    private TransactionStatus status;

    /**
     * Constructor with all fields.
     *
     * @param transactionId ID of the transaction
     * @param status        new status for the transaction
     */
    public TransactionApprovalRequest(Long transactionId, TransactionStatus status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    /**
     * Default constructor.
     */
    public TransactionApprovalRequest() {
    }

    /**
     * @return transaction ID
     */
    public Long getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId set transaction ID
     */
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return transaction status (e.g. APPROVED, REJECTED)
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * @param status set new transaction status
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}