package com.digitalwallet.walletservice.service;

import com.digitalwallet.walletservice.dto.DepositRequest;
import com.digitalwallet.walletservice.dto.TransactionApprovalRequest;
import com.digitalwallet.walletservice.dto.TransactionResponse;
import com.digitalwallet.walletservice.dto.WithDrawRequest;
import com.digitalwallet.walletservice.enums.TransactionStatus;
import com.digitalwallet.walletservice.enums.TransactionType;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Transaction;
import com.digitalwallet.walletservice.model.Wallet;
import com.digitalwallet.walletservice.repository.TransactionRepository;
import com.digitalwallet.walletservice.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing transactions including deposit, withdraw,
 * approve, and listing operations.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    /**
     * Constructs a new {@code TransactionServiceImpl} with the required repositories.
     *
     * @param transactionRepository repository for transaction persistence
     * @param walletRepository      repository for wallet persistence
     */
    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    /**
     * Handles the deposit process for a wallet.
     *
     * @param request the deposit request containing wallet ID and amount
     * @return the created {@link TransactionResponse}
     */
    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        authorizeWalletAccess(wallet);

        TransactionStatus status = determineStatus(request.getAmount());

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        if (status == TransactionStatus.APPROVED) {
            wallet.setUsableBalance(wallet.getUsableBalance().add(request.getAmount()));
        }

        Transaction transaction = createTransaction(wallet, request.getAmount(), TransactionType.DEPOSIT,
                status, request.getOppositeParty(), request.getOppositePartyType());

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    /**
     * Returns all transactions for the given wallet ID.
     *
     * @param walletId the wallet ID to retrieve transactions for
     * @return list of {@link TransactionResponse}
     */
    @Override
    public List<TransactionResponse> getTransactionsForWallet(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        authorizeWalletAccess(wallet);

        return transactionRepository.findByWallet(wallet).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Approves a pending transaction by updating its status and modifying wallet balances accordingly.
     *
     * @param request approval request containing transaction ID and new status
     * @return the updated {@link TransactionResponse}
     */
    @Override
    @Transactional
    public TransactionResponse approveTransaction(TransactionApprovalRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction is already processed.");
        }

        // Update transaction status
        transaction.setStatus(request.getStatus());

        if (TransactionStatus.APPROVED.equals(request.getStatus())) {
            Wallet wallet = transaction.getWallet();

            // For DEPOSIT: add to usable balance
            if (TransactionType.DEPOSIT.equals(transaction.getType())) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
            }

            // For WITHDRAW: already subtracted from usable_balance during creation, now subtract from balance
            if (TransactionType.WITHDRAW.equals(transaction.getType())) {
                wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
            }

            walletRepository.save(wallet);
        }

        Transaction updated = transactionRepository.save(transaction);
        return mapToResponse(updated);
    }

    /**
     * Processes a withdrawal from a wallet.
     *
     * @param request withdrawal request containing wallet ID and amount
     * @return the created {@link TransactionResponse}
     */
    @Override
    @Transactional
    public TransactionResponse withdraw(WithDrawRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        authorizeWalletAccess(wallet);

        if (!wallet.isActiveForWithdraw()) {
            throw new IllegalStateException("This wallet is not active for withdraw.");
        }

        TransactionStatus status = determineStatus(request.getAmount());

        if (status == TransactionStatus.APPROVED) {
            if (wallet.getUsableBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient usable balance");
            }
            wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(request.getAmount()));
        } else {
            if (wallet.getUsableBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient usable balance for pending transaction");
            }
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(request.getAmount()));
        }

        walletRepository.save(wallet);

        Transaction transaction = createTransaction(wallet, request.getAmount(), TransactionType.WITHDRAW,
                status, request.getOppositeParty(), request.getOppositePartyType());

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    /**
     * Verifies if the currently authenticated user has access to the given wallet.
     *
     * @param wallet the wallet to check access for
     */
    private void authorizeWalletAccess(Wallet wallet) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_CUSTOMER".equals(role)) {
            Customer currentCustomer = (Customer) auth.getPrincipal();
            if (!wallet.getCustomer().getId().equals(currentCustomer.getId())) {
                throw new AccessDeniedException("You are not allowed to access this wallet.");
            }
        }
    }

    /**
     * Creates a {@link Transaction} entity.
     *
     * @param wallet            the wallet associated with the transaction
     * @param amount            the transaction amount
     * @param type              the type of transaction (DEPOSIT or WITHDRAW)
     * @param status            the status of transaction (PENDING or APPROVED)
     * @param oppositeParty     the party involved in the transaction
     * @param oppositePartyType the type of opposite party
     * @return a new {@link Transaction} object
     */
    private Transaction createTransaction(Wallet wallet, BigDecimal amount, TransactionType type,
                                          TransactionStatus status, String oppositeParty,
                                          com.digitalwallet.walletservice.enums.OppositePartyType oppositePartyType) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setOppositePartyType(oppositePartyType);
        transaction.setOppositeParty(oppositeParty);
        transaction.setStatus(status);
        return transaction;
    }

    /**
     * Determines whether a transaction should be approved immediately or marked as pending.
     *
     * @param amount the amount of the transaction
     * @return {@link TransactionStatus#APPROVED} or {@link TransactionStatus#PENDING}
     */
    private TransactionStatus determineStatus(BigDecimal amount) {
        return amount.compareTo(BigDecimal.valueOf(1000)) > 0 ?
                TransactionStatus.PENDING : TransactionStatus.APPROVED;
    }

    /**
     * Maps a {@link Transaction} entity to its corresponding {@link TransactionResponse} DTO.
     *
     * @param transaction the transaction entity
     * @return the mapped {@link TransactionResponse}
     */
    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setWalletId(transaction.getWallet().getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setOppositePartyType(transaction.getOppositePartyType());
        response.setOppositeParty(transaction.getOppositeParty());
        response.setStatus(transaction.getStatus());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}