package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.OppositePartyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for handling withdrawal requests from a wallet.
 * This includes the wallet ID, amount to be withdrawn, and details of the opposite party.
 */
public class WithDrawRequest {

    /**
     * The ID of the wallet from which the withdrawal will be made.
     */
    @NotNull(message = "Wallet ID is required.")
    private Long walletId;

    /**
     * The amount of money to withdraw. Must be greater than zero.
     */
    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be positive. Please write positive amount.")
    private BigDecimal amount;

    /**
     * The type of the opposite party (e.g., INDIVIDUAL, MERCHANT).
     */
    @NotNull(message = "Opposite party type is required.")
    private OppositePartyType oppositePartyType;

    /**
     * The name or identifier of the opposite party.
     */
    @NotNull(message = "Opposite party is required.")
    private String oppositeParty;

    /**
     * Constructor to initialize all fields of the withdrawal request.
     *
     * @param walletId          ID of the wallet.
     * @param amount            Withdrawal amount.
     * @param oppositePartyType Type of the opposite party.
     * @param oppositeParty     Name or ID of the opposite party.
     */
    public WithDrawRequest(Long walletId, BigDecimal amount, OppositePartyType oppositePartyType, String oppositeParty) {
        this.walletId = walletId;
        this.amount = amount;
        this.oppositePartyType = oppositePartyType;
        this.oppositeParty = oppositeParty;
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
}