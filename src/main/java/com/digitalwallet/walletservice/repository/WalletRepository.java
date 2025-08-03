package com.digitalwallet.walletservice.repository;

import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.digitalwallet.walletservice.enums.Currency;

/**
 * Repository interface for managing {@link Wallet} entities.
 * Provides methods for performing CRUD operations and custom queries on wallets.
 */
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Finds all wallets associated with a specific customer.
     *
     * @param customer the customer whose wallets are to be retrieved
     * @return a list of wallets belonging to the specified customer
     */
    List<Wallet> findByCustomer(Customer customer);

    /**
     * Checks whether a wallet exists for the given customer and currency.
     *
     * @param customerId the ID of the customer
     * @param currency   the currency type
     * @return true if a wallet with the specified currency exists for the customer, false otherwise
     */
    boolean existsByCustomerIdAndCurrency(Long customerId, Currency currency);
}