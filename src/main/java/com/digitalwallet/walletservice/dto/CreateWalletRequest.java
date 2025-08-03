package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO used to represent the request payload when creating a new wallet.
 */
public class CreateWalletRequest {

    /**
     * Name of the wallet to be created.
     */
    @NotBlank
    private String walletName;

    /**
     * Currency type for the wallet. Must be one of the supported {@link Currency} values.
     */
    @NotNull
    private Currency currency;

    /**
     * Indicates whether the wallet is active for shopping transactions.
     */
    private boolean activeForShopping;

    /**
     * Indicates whether the wallet is active for withdraw operations.
     */
    private boolean activeForWithdraw;

    /**
     * Constructor for creating a new CreateWalletRequest.
     *
     * @param walletName        the name of the wallet.
     * @param currency          the currency type (TRY, USD, EUR).
     * @param activeForShopping whether the wallet can be used for shopping.
     * @param activeForWithdraw whether the wallet can be used for withdrawing.
     */
    public CreateWalletRequest(String walletName, Currency currency, boolean activeForShopping, boolean activeForWithdraw) {
        this.walletName = walletName;
        this.currency = currency;
        this.activeForShopping = activeForShopping;
        this.activeForWithdraw = activeForWithdraw;
    }

    /**
     * Returns the name of the wallet.
     *
     * @return wallet name.
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * Sets the name of the wallet.
     *
     * @param walletName the wallet name to set.
     */
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    /**
     * Returns the currency of the wallet.
     *
     * @return wallet currency.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency of the wallet.
     *
     * @param currency the wallet currency to set.
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Indicates whether the wallet is active for shopping.
     *
     * @return true if active for shopping, false otherwise.
     */
    public boolean isActiveForShopping() {
        return activeForShopping;
    }

    /**
     * Sets whether the wallet is active for shopping.
     *
     * @param activeForShopping flag indicating shopping status.
     */
    public void setActiveForShopping(boolean activeForShopping) {
        this.activeForShopping = activeForShopping;
    }

    /**
     * Indicates whether the wallet is active for withdraw operations.
     *
     * @return true if active for withdraw, false otherwise.
     */
    public boolean isActiveForWithdraw() {
        return activeForWithdraw;
    }

    /**
     * Sets whether the wallet is active for withdraw operations.
     *
     * @param activeForWithdraw flag indicating withdraw status.
     */
    public void setActiveForWithdraw(boolean activeForWithdraw) {
        this.activeForWithdraw = activeForWithdraw;
    }
}