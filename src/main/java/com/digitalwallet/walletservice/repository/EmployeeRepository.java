package com.digitalwallet.walletservice.repository;

import com.digitalwallet.walletservice.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Employee} entities.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their email address.
     *
     * @param email the email address of the employee
     * @return an {@link Optional} containing the found employee, or empty if not found
     */
    Optional<Employee> findByEmail(String email);
}