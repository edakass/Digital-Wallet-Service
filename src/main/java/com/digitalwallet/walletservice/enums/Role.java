package com.digitalwallet.walletservice.enums;

/**
 * Enum representing the user roles within the system.

 * Used for role-based access control in the authentication and authorization processes.
 */
public enum Role {

    /**
     * Customer role - can perform operations only on their own data.
     */
    CUSTOMER,

    /**
     * Employee role - can perform operations on behalf of all customers.
     */
    EMPLOYEE
}