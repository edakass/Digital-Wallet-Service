package com.digitalwallet.walletservice.service;

import com.digitalwallet.walletservice.dto.CreateWalletRequest;
import com.digitalwallet.walletservice.dto.CustomerWithWalletsResponse;
import com.digitalwallet.walletservice.dto.WalletResponse;

import java.util.List;

/**
 * Service interface for wallet-related operations such as wallet creation and retrieval.
 */
public interface WalletService {

    /**
     * Creates a new wallet for the specified customer.
     *
     * @param request    the wallet creation request containing wallet name, currency, and flags
     * @param customerId the ID of the customer who will own the wallet
     * @return the created {@link WalletResponse}
     */
    WalletResponse createWallet(CreateWalletRequest request, Long customerId);

    /**
     * Retrieves all wallets belonging to the specified customer.
     * Typically used by EMPLOYEE users to view other users' wallets.
     *
     * @param customerId the ID of the customer
     * @return a list of {@link WalletResponse} objects representing the customer's wallets
     */
    List<WalletResponse> listWalletsCustomer(Long customerId);

    /**
     * Retrieves all wallets belonging to the currently authenticated CUSTOMER.
     * Used when a CUSTOMER wants to view their own wallets.
     *
     * @return a list of {@link WalletResponse} objects representing the logged-in user's wallets
     */
    List<WalletResponse> listWalletsCustomerByToken();

    /**
     * Retrieves all wallets in the system, grouped by their respective customers.
     * <p>
     * Typically used by EMPLOYEE users to view all customer-wallet relationships.
     *
     * @return a list of {@link CustomerWithWalletsResponse} objects
     * where each entry contains customer info and associated wallets
     */
    List<CustomerWithWalletsResponse> listAllWalletsGroupedByCustomer();
}