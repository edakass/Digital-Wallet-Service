package com.digitalwallet.walletservice.dto;

/**
 * DTO class representing a login request for a customer.
 * Used to authenticate the user and generate a JWT token.
 */
public class LoginRequest {

    /**
     * Turkish National ID number (TCKN) of the customer.
     */
    private String tckn;

    /**
     * Plaintext password of the customer.
     */
    private String password;

    /**
     * Default constructor.
     */
    public LoginRequest() {
    }

    /**
     * Parameterized constructor to initialize login credentials.
     *
     * @param tckn     Turkish National ID number.
     * @param password Customer's password.
     */
    public LoginRequest(String tckn, String password) {
        this.tckn = tckn;
        this.password = password;
    }

    /**
     * Gets the TCKN.
     *
     * @return TCKN (Turkish ID number).
     */
    public String getTckn() {
        return tckn;
    }

    /**
     * Sets the TCKN.
     *
     * @param tckn the Turkish ID number to set.
     */
    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    /**
     * Gets the password.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}