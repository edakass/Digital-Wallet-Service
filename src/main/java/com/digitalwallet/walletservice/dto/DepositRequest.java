package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.OppositePartyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO used to represent a deposit transaction request into a wallet.
 */
public class DepositRequest {

    /**
     * ID of the wallet where the deposit will be made.
     */
    @NotNull
    private Long walletId;

    /**
     * Amount to deposit. Must be a positive decimal value.
     */
    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be positive. Please write positive amount")
    private BigDecimal amount;

    /**
     * The name or identifier of the opposite party involved in the transaction.
     */
    @NotBlank
    private String oppositeParty;

    /**
     * The type of the opposite party (e.g., INDIVIDUAL, COMPANY).
     */
    @NotNull
    private OppositePartyType oppositePartyType;

    /**
     * Constructor to initialize all fields.
     *
     * @param walletId          ID of the wallet.
     * @param amount            Amount to deposit.
     * @param oppositeParty     Opposite party's name or identifier.
     * @param oppositePartyType Type of the opposite party.
     */
    public DepositRequest(Long walletId, BigDecimal amount, String oppositeParty, OppositePartyType oppositePartyType) {
        this.walletId = walletId;
        this.amount = amount;
        this.oppositeParty = oppositeParty;
        this.oppositePartyType = oppositePartyType;
    }

    /**
     * Returns the wallet ID.
     *
     * @return wallet ID.
     */
    public Long getWalletId() {
        return walletId;
    }

    /**
     * Sets the wallet ID.
     *
     * @param walletId the wallet ID to set.
     */
    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    /**
     * Returns the deposit amount.
     *
     * @return deposit amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the deposit amount.
     *
     * @param amount the amount to deposit.
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Returns the opposite party.
     *
     * @return opposite party.
     */
    public String getOppositeParty() {
        return oppositeParty;
    }

    /**
     * Sets the opposite party.
     *
     * @param oppositeParty the opposite party to set.
     */
    public void setOppositeParty(String oppositeParty) {
        this.oppositeParty = oppositeParty;
    }

    /**
     * Returns the opposite party type.
     *
     * @return opposite party type.
     */
    public OppositePartyType getOppositePartyType() {
        return oppositePartyType;
    }

    /**
     * Sets the opposite party type.
     *
     * @param oppositePartyType the opposite party type to set.
     */
    public void setOppositePartyType(OppositePartyType oppositePartyType) {
        this.oppositePartyType = oppositePartyType;
    }
}