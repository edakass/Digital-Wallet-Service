package com.digitalwallet.walletservice.dto;

import com.digitalwallet.walletservice.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the payload for registering a new user (either CUSTOMER or EMPLOYEE).
 * <p>
 * Required fields depend on the selected {@link Role}:
 * <ul>
 *   <li><strong>CUSTOMER</strong>: {@code name}, {@code surname}, {@code tckn}, {@code password}, {@code role}</li>
 *   <li><strong>EMPLOYEE</strong>: {@code name}, {@code surname}, {@code email}, {@code password}, {@code role}</li>
 * </ul>
 */
public class RegisterRequest {

    /**
     * First name of the user.
     */
    @NotBlank
    private String name;

    /**
     * Last name (surname) of the user.
     */
    @NotBlank
    private String surname;

    /**
     * Turkish National Identification Number (required for CUSTOMER).
     */
    private String tckn;

    /**
     * Email address (required for EMPLOYEE).
     */
    private String email;

    /**
     * Plaintext password to be encrypted before storing.
     */
    @NotBlank
    private String password;

    /**
     * Role of the user: CUSTOMER or EMPLOYEE.
     */
    @NotNull
    private Role role;

    /**
     * Constructs a {@code RegisterRequest} with all required fields.
     *
     * @param name     user's first name
     * @param surname  user's last name
     * @param tckn     user's national ID (CUSTOMER only)
     * @param email    user's email address (EMPLOYEE only)
     * @param password user's password
     * @param role     user's role (CUSTOMER or EMPLOYEE)
     */
    public RegisterRequest(String name, String surname, String tckn, String email, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.tckn = tckn;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * @return user's first name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name set user's first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return user's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname set user's last name
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return user's Turkish national ID number (TCKN)
     */
    public String getTckn() {
        return tckn;
    }

    /**
     * @param tckn set user's national ID number
     */
    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    /**
     * @return password (in plaintext, to be encoded before storing)
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password set the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return role of the user (CUSTOMER or EMPLOYEE)
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role set the user's role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}