package com.digitalwallet.walletservice.model;

import com.digitalwallet.walletservice.enums.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Entity class representing a digital wallet.
 * Each wallet belongs to a customer and supports multiple currencies.
 */
@Entity
public class Wallet {

    /**
     * Primary key of the wallet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the wallet.
     */
    private String walletName;

    /**
     * Currency type of the wallet (e.g., TRY, USD, EUR).
     */
    @Enumerated(EnumType.STRING)
    private Currency currency;

    /**
     * Indicates whether this wallet is active for shopping transactions.
     */
    private boolean activeForShopping;

    /**
     * Indicates whether this wallet is active for withdraw transactions.
     */
    private boolean activeForWithdraw;

    /**
     * Total balance in the wallet.
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Usable balance (may differ if some funds are pending approval).
     */
    private BigDecimal usableBalance = BigDecimal.ZERO;

    /**
     * Customer who owns the wallet.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * Default constructor required by JPA.
     */
    public Wallet() {}

    /**
     * All-args constructor.
     *
     * @param id               wallet ID
     * @param walletName       name of the wallet
     * @param currency         currency type
     * @param activeForShopping whether wallet is active for shopping
     * @param activeForWithdraw whether wallet is active for withdrawal
     * @param balance          total balance
     * @param usableBalance    usable (available) balance
     * @param customer         owning customer
     */
    public Wallet(Long id, String walletName, Currency currency, boolean activeForShopping,
                  boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance, Customer customer) {
        this.id = id;
        this.walletName = walletName;
        this.currency = currency;
        this.activeForShopping = activeForShopping;
        this.activeForWithdraw = activeForWithdraw;
        this.balance = balance;
        this.usableBalance = usableBalance;
        this.customer = customer;
    }

    /**
     * Gets the wallet ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the wallet ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the wallet name.
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * Sets the wallet name.
     */
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    /**
     * Gets the currency type.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Sets the currency type.
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Checks if wallet is active for shopping.
     */
    public boolean isActiveForShopping() {
        return activeForShopping;
    }

    /**
     * Sets the active-for-shopping flag.
     */
    public void setActiveForShopping(boolean activeForShopping) {
        this.activeForShopping = activeForShopping;
    }

    /**
     * Checks if wallet is active for withdraw.
     */
    public boolean isActiveForWithdraw() {
        return activeForWithdraw;
    }

    /**
     * Sets the active-for-withdraw flag.
     */
    public void setActiveForWithdraw(boolean activeForWithdraw) {
        this.activeForWithdraw = activeForWithdraw;
    }

    /**
     * Gets the total balance.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the total balance.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Gets the usable balance.
     */
    public BigDecimal getUsableBalance() {
        return usableBalance;
    }

    /**
     * Sets the usable balance.
     */
    public void setUsableBalance(BigDecimal usableBalance) {
        this.usableBalance = usableBalance;
    }

    /**
     * Gets the customer who owns the wallet.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer who owns the wallet.
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
