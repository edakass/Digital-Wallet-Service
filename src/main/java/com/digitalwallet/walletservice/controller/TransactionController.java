package com.digitalwallet.walletservice.controller;

import com.digitalwallet.walletservice.dto.DepositRequest;
import com.digitalwallet.walletservice.dto.TransactionApprovalRequest;
import com.digitalwallet.walletservice.dto.TransactionResponse;
import com.digitalwallet.walletservice.dto.WithDrawRequest;
import com.digitalwallet.walletservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling transaction operations such as deposit, withdraw,
 * approval and listing transactions for a wallet.
 * <p>
 * Base path: /api/transactions
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructor for injecting {@link TransactionService}.
     *
     * @param transactionService the service handling transaction operations
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Endpoint to create a deposit transaction.
     * <p>
     * Deposits greater than 1000 will be marked as PENDING, otherwise APPROVED.
     *
     * @param request the deposit request containing wallet ID, amount, and source
     * @return the created transaction response
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to approve or deny a pending transaction.
     *
     * @param request contains the transaction ID and desired status
     * @return the updated transaction response
     */
    @PostMapping("/approve")
    public ResponseEntity<TransactionResponse> approveTransaction(
            @Valid @RequestBody TransactionApprovalRequest request) {
        TransactionResponse response = transactionService.approveTransaction(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to create a withdraw transaction.
     * <p>
     * Checks whether wallet is active for withdraw.
     *
     * @param request the withdraw request including wallet ID, amount, and destination
     * @return the created transaction response
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithDrawRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to list all transactions for a given wallet ID.
     *
     * @param walletId the ID of the wallet
     * @return list of transactions associated with the wallet
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Long walletId) {
        return ResponseEntity.ok(transactionService.getTransactionsForWallet(walletId));
    }
}