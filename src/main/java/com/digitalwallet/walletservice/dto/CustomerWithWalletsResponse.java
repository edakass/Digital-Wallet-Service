package com.digitalwallet.walletservice.dto;

import java.util.List;

/**
 * DTO representing a customer along with their associated wallets.
 * <p>
 * Typically used in responses where an EMPLOYEE is viewing wallet data grouped by customer.
 */
public class CustomerWithWalletsResponse {

    /**
     * Unique identifier of the customer.
     */
    private Long customerId;

    /**
     * First name of the customer.
     */
    private String name;

    /**
     * Last name (surname) of the customer.
     */
    private String surname;

    /**
     * Turkish National Identification Number of the customer.
     */
    private String tckn;

    /**
     * List of wallets owned by the customer.
     */
    private List<WalletResponse> wallets;


    /**
     * Constructs a {@code CustomerWithWalletsResponse} with the provided customer and wallet data.
     *
     * @param customerId the ID of the customer
     * @param name       the customer's first name
     * @param surname    the customer's last name
     * @param tckn       the customer's Turkish National ID
     * @param wallets    the list of the customer's wallets
     */
    public CustomerWithWalletsResponse(Long customerId, String name, String surname, String tckn,
                                       List<WalletResponse> wallets) {
        this.customerId = customerId;
        this.name = name;
        this.surname = surname;
        this.tckn = tckn;
        this.wallets = wallets;
    }

    public CustomerWithWalletsResponse() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public List<WalletResponse> getWallets() {
        return wallets;
    }

    public void setWallets(List<WalletResponse> wallets) {
        this.wallets = wallets;
    }
}