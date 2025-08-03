package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.OppositePartyType;
import com.digitalwallet.walletservice.enums.TransactionStatus;
import com.digitalwallet.walletservice.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO class that represents the response of a transaction.
 * Used to return detailed information about a completed deposit, withdrawal, or other wallet transaction.
 */
public class TransactionResponse {

    /**
     * Unique identifier of the transaction.
     */
    private Long id;

    /**
     * Identifier of the wallet associated with this transaction.
     */
    private Long walletId;

    /**
     * The amount of money involved in the transaction.
     */
    private BigDecimal amount;

    /**
     * The type of transaction (e.g., DEPOSIT, WITHDRAW).
     */
    private TransactionType type;

    /**
     * The type of the other party involved in the transaction (e.g., PERSON, COMPANY).
     */
    private OppositePartyType oppositePartyType;

    /**
     * The name or identifier of the opposite party.
     */
    private String oppositeParty;

    /**
     * The status of the transaction (e.g., PENDING, APPROVED, REJECTED).
     */
    private TransactionStatus status;

    /**
     * The date and time when the transaction was created.
     */
    private LocalDateTime createdAt;

    /**
     * Constructor to initialize all fields of the transaction response.
     */
    public TransactionResponse(Long id, Long walletId, BigDecimal amount, TransactionType type,
                               OppositePartyType oppositePartyType, String oppositeParty, TransactionStatus status,
                               LocalDateTime createdAt) {
        this.id = id;
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.oppositePartyType = oppositePartyType;
        this.oppositeParty = oppositeParty;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Default constructor.
     */
    public TransactionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public OppositePartyType getOppositePartyType() {
        return oppositePartyType;
    }

    public void setOppositePartyType(OppositePartyType oppositePartyType) {
        this.oppositePartyType = oppositePartyType;
    }

    public String getOppositeParty() {
        return oppositeParty;
    }

    public void setOppositeParty(String oppositeParty) {
        this.oppositeParty = oppositeParty;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}