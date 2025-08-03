package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.Currency;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the response for a wallet.
 * It includes detailed information such as balances, currency, and wallet settings.
 */
public class WalletResponse {

    /**
     * The unique identifier of the wallet.
     */
    private Long id;

    /**
     * The display name of the wallet.
     */
    private String walletName;

    /**
     * The currency type used in this wallet (e.g., TRY, USD, EUR).
     */
    private Currency currency;

    /**
     * Indicates whether this wallet is active for shopping transactions.
     */
    private boolean activeForShopping;

    /**
     * Indicates whether this wallet is active for withdrawal transactions.
     */
    private boolean activeForWithdraw;

    /**
     * The total balance in the wallet.
     */
    private BigDecimal balance;

    /**
     * The balance that can currently be used for transactions (e.g., shopping or withdrawal).
     */
    private BigDecimal usableBalance;

    /**
     * Constructor to initialize all fields of the wallet response.
     */
    public WalletResponse(Long id, String walletName, Currency currency, boolean activeForShopping,
                          boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance) {
        this.id = id;
        this.walletName = walletName;
        this.currency = currency;
        this.activeForShopping = activeForShopping;
        this.activeForWithdraw = activeForWithdraw;
        this.balance = balance;
        this.usableBalance = usableBalance;
    }

    /**
     * Default constructor.
     */
    public WalletResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isActiveForShopping() {
        return activeForShopping;
    }

    public void setActiveForShopping(boolean activeForShopping) {
        this.activeForShopping = activeForShopping;
    }

    public boolean isActiveForWithdraw() {
        return activeForWithdraw;
    }

    public void setActiveForWithdraw(boolean activeForWithdraw) {
        this.activeForWithdraw = activeForWithdraw;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getUsableBalance() {
        return usableBalance;
    }

    public void setUsableBalance(BigDecimal usableBalance) {
        this.usableBalance = usableBalance;
    }
}