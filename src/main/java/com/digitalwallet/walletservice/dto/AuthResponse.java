package com.digitalwallet.walletservice.dto;

/**
 * A simple response DTO used for returning JWT token after successful authentication.
 */
public class AuthResponse {

    /**
     * JWT token to be used for authenticated requests.
     */
    private String token;

    /**
     * Constructs a new AuthResponse with the provided token.
     *
     * @param token The JWT token string.
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Returns the JWT token.
     *
     * @return the JWT token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the JWT token.
     *
     * @param token the JWT token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }
}