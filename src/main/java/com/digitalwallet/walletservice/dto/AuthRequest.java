package com.digitalwallet.walletservice.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for handling authentication requests including login and registration.
 * <p>
 * Depending on the user's role:
 * <ul>
 *   <li><strong>CUSTOMER:</strong> must provide {@code tckn}, {@code name}, {@code surname}, and {@code password}</li>
 *   <li><strong>EMPLOYEE:</strong> must provide {@code email}, {@code name}, {@code surname}, and {@code password}</li>
 * </ul>
 */
public class AuthRequest {

    /**
     * Turkish National Identification Number (required for CUSTOMER).
     */
    private String tckn;

    /**
     * Email address (required for EMPLOYEE).
     */
    private String email;

    /**
     * Password used for authentication.
     */
    @NotBlank(message = "Password is required.")
    private String password;

    /**
     * User role. Expected values are: {@code CUSTOMER} or {@code EMPLOYEE}.
     */
    @NotBlank(message = "Role is required.")
    private String role;

    /**
     * First name of the user.
     */
    private String name;

    /**
     * Last name of the user.
     */
    private String surname;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}