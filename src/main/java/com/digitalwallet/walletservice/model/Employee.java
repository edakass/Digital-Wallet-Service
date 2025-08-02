package com.digitalwallet.walletservice.model;

import jakarta.persistence.*;

/**
 * Entity class representing an Employee in the system.
 * This class is mapped to a database table via JPA.
 */
@Entity
public class Employee {

    /**
     * Primary key of the employee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employee's first name.
     */
    private String name;

    /**
     * Employee's last name.
     */
    private String surname;

    /**
     * Encrypted password of the employee.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Unique email of the employee, used for login.
     */
    @Column(unique = true)
    private String email;

    /**
     * Default constructor required by JPA.
     */
    public Employee() {}

    /**
     * Constructor with all fields.
     *
     * @param id       employee ID
     * @param name     employee's first name
     * @param surname  employee's last name
     * @param password employee's encrypted password
     * @param email    employee's unique email
     */
    public Employee(Long id, String name, String surname, String password, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
    }

    /**
     * Gets the employee's ID.
     *
     * @return employee ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the employee's ID.
     *
     * @param id employee ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the employee's first name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the employee's first name.
     *
     * @param name first name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the employee's last name.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the employee's last name.
     *
     * @param surname last name
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the employee's encrypted password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the employee's password.
     *
     * @param password encrypted password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the employee's email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the employee's email.
     *
     * @param email unique email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
}