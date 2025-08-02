package com.digitalwallet.walletservice.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity class representing a Customer in the system.
 * Implements {@link UserDetails} for Spring Security authentication.
 */
@Entity
public class Customer implements UserDetails {

    /**
     * Primary key of the customer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Customer's first name.
     */
    private String name;

    /**
     * Customer's last name.
     */
    private String surname;

    /**
     * Encrypted password of the customer.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Unique identifier (like national ID), limited to 11 characters.
     */
    @Column(unique = true, length = 11)
    private String tckn;

    /**
     * Default constructor required by JPA.
     */
    public Customer() {}

    /**
     * Constructor with all fields except password.
     *
     * @param id     customer ID
     * @param name   customer's name
     * @param surname customer's surname
     * @param tckn   customer's TCKN
     */
    public Customer(Long id, String name, String surname, String tckn) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.tckn = tckn;
    }

    /**
     * Constructor with all fields.
     *
     * @param id       customer ID
     * @param name     customer's name
     * @param surname  customer's surname
     * @param password customer's encrypted password
     * @param tckn     customer's TCKN
     */
    public Customer(Long id, String name, String surname, String password, String tckn) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.tckn = tckn;
    }

    /**
     * Gets the customer's ID.
     *
     * @return customer ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the customer's ID.
     *
     * @param id customer ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the customer's name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the customer's name.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the customer's surname.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the customer's surname.
     *
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the customer's password.
     *
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the customer's password.
     *
     * @param password password (encrypted)
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the customer's TCKN.
     *
     * @return TCKN
     */
    public String getTckn() {
        return tckn;
    }

    /**
     * Sets the customer's TCKN.
     *
     * @param tckn TCKN
     */
    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    /**
     * Returns the authorities granted to the customer.
     *
     * @return list with ROLE_CUSTOMER authority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    /**
     * Returns the username used to authenticate the customer.
     * In this case, the TCKN is used as the username.
     *
     * @return TCKN as username
     */
    @Override
    public String getUsername() {
        return tckn;
    }

    /**
     * Indicates whether the user's account has expired.
     * Always returns true for now.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Always returns true for now.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * Always returns true for now.
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Always returns true for now.
     *
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}