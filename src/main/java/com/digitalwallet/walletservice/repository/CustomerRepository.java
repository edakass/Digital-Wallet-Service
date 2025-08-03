package com.digitalwallet.walletservice.repository;

import com.digitalwallet.walletservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Customer} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Retrieves a customer by their TCKN (Turkish Identity Number).
     *
     * @param tckn the Turkish Citizenship Number of the customer
     * @return an {@link Optional} containing the customer if found, otherwise empty
     */
    Optional<Customer> findByTckn(String tckn);
}