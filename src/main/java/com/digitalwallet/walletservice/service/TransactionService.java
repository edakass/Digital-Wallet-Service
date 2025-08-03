package com.digitalwallet.walletservice.service;

import com.digitalwallet.walletservice.dto.DepositRequest;
import com.digitalwallet.walletservice.dto.TransactionApprovalRequest;
import com.digitalwallet.walletservice.dto.TransactionResponse;
import com.digitalwallet.walletservice.dto.WithDrawRequest;

import java.util.List;

/**
 * Service interface that defines operations related to wallet transactions.
 * Includes deposit, withdrawal, transaction approval, and listing transactions for a wallet.
 */
public interface TransactionService {

    /**
     * Performs a deposit transaction for a given wallet.
     *
     * @param request the deposit request containing wallet ID, amount, and opposite party info
     * @return {@link TransactionResponse} representing the created deposit transaction
     */
    TransactionResponse deposit(DepositRequest request);

    /**
     * Retrieves all transactions associated with a specific wallet.
     *
     * @param walletId the ID of the wallet
     * @return list of {@link TransactionResponse} for the specified wallet
     */
    List<TransactionResponse> getTransactionsForWallet(Long walletId);

    /**
     * Approves or rejects a pending transaction based on the provided status.
     *
     * @param request the approval request containing transaction ID and new status
     * @return {@link TransactionResponse} representing the updated transaction
     */
    TransactionResponse approveTransaction(TransactionApprovalRequest request);

    /**
     * Performs a withdrawal transaction for a given wallet.
     *
     * @param request the withdrawal request containing wallet ID, amount, and opposite party info
     * @return {@link TransactionResponse} representing the created withdrawal transaction
     */
    TransactionResponse withdraw(WithDrawRequest request);
}