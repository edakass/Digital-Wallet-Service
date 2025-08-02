package com.digitalwallet.walletservice.model;

import com.digitalwallet.walletservice.enums.OppositePartyType;
import com.digitalwallet.walletservice.enums.TransactionStatus;
import com.digitalwallet.walletservice.enums.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Entity class representing a financial transaction such as deposit or withdraw.
 */
@Entity
public class Transaction {

    /**
     * Primary key of the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Amount of money involved in the transaction.
     */
    private BigDecimal amount;

    /**
     * Type of transaction: DEPOSIT or WITHDRAW.
     */
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * Type of the opposite party: IBAN or PAYMENT.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "opposite_party_type")
    private OppositePartyType oppositePartyType;

    /**
     * Identifier of the opposite party (e.g., IBAN number or payment ID).
     */
    private String oppositeParty;

    /**
     * Status of the transaction: PENDING, APPROVED, or DENIED.
     */
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    /**
     * Timestamp when the transaction was created.
     */
    private LocalDateTime createdAt;

    /**
     * The wallet associated with the transaction.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    /**
     * Sets the createdAt timestamp just before persisting to database.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Default constructor required by JPA.
     */
    public Transaction() {}

    /**
     * Constructor with all fields.
     *
     * @param id                 transaction ID
     * @param amount             transaction amount
     * @param type               transaction type (DEPOSIT or WITHDRAW)
     * @param oppositePartyType  type of the opposite party (IBAN or PAYMENT)
     * @param oppositeParty      identifier for the opposite party
     * @param status             transaction status
     * @param createdAt          timestamp of creation
     * @param wallet             associated wallet
     */
    public Transaction(Long id, BigDecimal amount, TransactionType type,
                       OppositePartyType oppositePartyType, String oppositeParty,
                       TransactionStatus status, LocalDateTime createdAt, Wallet wallet) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.oppositePartyType = oppositePartyType;
        this.oppositeParty = oppositeParty;
        this.status = status;
        this.createdAt = createdAt;
        this.wallet = wallet;
    }

    /**
     * Gets the transaction ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the transaction ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the transaction amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the transaction type.
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Sets the transaction type.
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Gets the opposite party type.
     */
    public OppositePartyType getOppositePartyType() {
        return oppositePartyType;
    }

    /**
     * Sets the opposite party type.
     */
    public void setOppositePartyType(OppositePartyType oppositePartyType) {
        this.oppositePartyType = oppositePartyType;
    }

    /**
     * Gets the opposite party identifier.
     */
    public String getOppositeParty() {
        return oppositeParty;
    }

    /**
     * Sets the opposite party identifier.
     */
    public void setOppositeParty(String oppositeParty) {
        this.oppositeParty = oppositeParty;
    }

    /**
     * Gets the transaction status.
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * Sets the transaction status.
     */
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * Gets the creation timestamp.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the associated wallet.
     */
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * Sets the associated wallet.
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}