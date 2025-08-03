package com.digitalwallet.walletservice.controller;

import com.digitalwallet.walletservice.dto.CreateWalletRequest;
import com.digitalwallet.walletservice.dto.CustomerWithWalletsResponse;
import com.digitalwallet.walletservice.dto.WalletResponse;
import com.digitalwallet.walletservice.exception.AccessDeniedCustomException;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for handling wallet-related operations such as creation,
 * listing, and approval of transactions.
 * <p>
 * Base path: <code>/api/auth/wallets</code>
 */

@RestController
@RequestMapping("/api/auth/wallets")
public class WalletController {

    private final WalletService walletService;

    /**
     * Constructor for injecting WalletService.
     *
     * @param walletService the wallet service dependency
     */
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * Creates a wallet for the authenticated user.
     * <p>
     * If the user is a CUSTOMER, the wallet will be created for themselves.<br>
     * If the user is an EMPLOYEE, the {@code customerId} parameter must be provided.
     *
     * @param request    the wallet creation request
     * @param customerId (optional) the ID of the customer for whom the wallet is created (EMPLOYEE only)
     * @return the created {@link WalletResponse}
     * @throws IllegalArgumentException if an EMPLOYEE does not provide a customerId
     */

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
    @PostMapping
    public WalletResponse createWallet(@RequestBody @Valid CreateWalletRequest request,
                                       @RequestParam(required = false) Long customerId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_CUSTOMER")) {
            Customer currentCustomer = (Customer) auth.getPrincipal();
            customerId = currentCustomer.getId();
        } else if (customerId == null) {
            throw new IllegalArgumentException("Employee must provide customerId");
        }

        return walletService.createWallet(request, customerId);
    }

    /**
     * Lists wallets based on the role of the authenticated user.
     * <ul>
     *   <li>If the user is a CUSTOMER, only their own wallets are returned.</li>
     *   <li>If the user is an EMPLOYEE, they must provide a {@code customerId} to query that customer's wallets.</li>
     * </ul>
     *
     * @param customerId (optional) ID of the customer to fetch wallets for (EMPLOYEE only)
     * @return list of {@link WalletResponse} objects
     * @throws AccessDeniedCustomException if a CUSTOMER attempts to access another user's wallets
     * @throws IllegalArgumentException    if EMPLOYEE does not provide customerId
     */
    @GetMapping
    public ResponseEntity<List<WalletResponse>> listWallets(@RequestParam(required = false) Long customerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_CUSTOMER")) {
            if (customerId != null) {
                throw new AccessDeniedCustomException("Unauthorized operation: " +
                        "Users with the CUSTOMER role cannot view someone else's wallet.");
            }

            return ResponseEntity.ok(walletService.listWalletsCustomerByToken());
        } else {
            if (customerId == null) {
                throw new IllegalArgumentException("The 'customerId' parameter is required for the EMPLOYEE role. " +
                        "Example: /wallets?customerId=3");
            }
            return ResponseEntity.ok(walletService.listWalletsCustomer(customerId));
        }
    }

    /**
     * Endpoint for approving a transaction.
     * <p>
     * This method is a placeholder and does not currently perform any business logic.
     *
     * @param transactionId the ID of the transaction to update
     * @param status        the new status (e.g., APPROVED, DENIED)
     * @return a simple confirmation message
     */
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/approve")
    public ResponseEntity<String> approveTransaction(@RequestParam Long transactionId, @RequestParam String status) {
        return ResponseEntity.ok("Approved");
    }


    /**
     * Lists all wallets for all customers.
     * <p>
     * Only accessible by users with the EMPLOYEE role.
     *
     * @return a list of {@link CustomerWithWalletsResponse} containing customers and their associated wallets
     */
    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/all")
    public ResponseEntity<List<CustomerWithWalletsResponse>> listAllWalletsForAllCustomers() {
        return ResponseEntity.ok(walletService.listAllWalletsGroupedByCustomer());
    }
}