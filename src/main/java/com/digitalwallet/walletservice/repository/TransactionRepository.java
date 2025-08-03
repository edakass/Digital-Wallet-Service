package com.digitalwallet.walletservice.repository;

import com.digitalwallet.walletservice.model.Transaction;
import com.digitalwallet.walletservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Transaction} entities.
 * Provides methods for performing CRUD operations on transactions.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions associated with a given wallet.
     *
     * @param wallet the wallet whose transactions are to be retrieved
     * @return a list of transactions linked to the specified wallet
     */
    List<Transaction> findByWallet(Wallet wallet);
}