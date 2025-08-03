package com.digitalwallet.walletservice.service;


import com.digitalwallet.walletservice.dto.CreateWalletRequest;
import com.digitalwallet.walletservice.dto.CustomerWithWalletsResponse;
import com.digitalwallet.walletservice.dto.WalletResponse;
import com.digitalwallet.walletservice.exception.WalletAlreadyExistsException;
import com.digitalwallet.walletservice.exception.WalletNotFoundException;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Wallet;
import com.digitalwallet.walletservice.repository.CustomerRepository;
import com.digitalwallet.walletservice.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Implementation of {@link WalletService} that handles wallet creation and retrieval
 * for both authenticated customers and employees.
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    /**
     * Constructs a {@code WalletServiceImpl} with required repositories.
     *
     * @param walletRepository   the repository for managing wallets
     * @param customerRepository the repository for accessing customer data
     */
    public WalletServiceImpl(WalletRepository walletRepository, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Creates a new wallet for a specified customer.
     *
     * @param request    the request containing wallet details
     * @param customerId the ID of the customer to associate the wallet with
     * @return the created {@link WalletResponse}
     * @throws WalletAlreadyExistsException if the customer already has a wallet in the same currency
     * @throws IllegalArgumentException     if the customer is not found
     */
    @Override
    public WalletResponse createWallet(CreateWalletRequest request, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (walletRepository.existsByCustomerIdAndCurrency(customerId, request.getCurrency())) {
            throw new WalletAlreadyExistsException("This customer already has a wallet with currency: " + request.getCurrency());
        }

        Wallet wallet = new Wallet();
        wallet.setWalletName(request.getWalletName());
        wallet.setCurrency(request.getCurrency());
        wallet.setActiveForShopping(request.isActiveForShopping());
        wallet.setActiveForWithdraw(request.isActiveForWithdraw());
        wallet.setBalance(request.getCurrency() != null ? BigDecimal.ZERO : null);
        wallet.setUsableBalance(request.getCurrency() != null ? BigDecimal.ZERO : null);
        wallet.setCustomer(customer);

        Wallet savedWallet = walletRepository.save(wallet);
        return mapToResponse(savedWallet);
    }

    /**
     * Lists all wallets associated with a specific customer.
     * This method is typically restricted to ROLE_EMPLOYEE.
     *
     * @param customerId the ID of the customer
     * @return list of {@link WalletResponse} objects
     * @throws IllegalArgumentException if the customer is not found
     * @throws WalletNotFoundException  if the customer has no wallets
     */
    @Override
    public List<WalletResponse> listWalletsCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found."));

        List<Wallet> wallets = walletRepository.findByCustomer(customer);

        if (wallets.isEmpty()) {
            throw new WalletNotFoundException("This customer has no wallets.");
        }

        return wallets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all wallets associated with the currently authenticated customer.
     * <p>
     * Uses the Spring Security context to obtain the logged-in user's {@link Customer} object.
     *
     * @return list of {@link WalletResponse} representing the user's wallets
     */
    @Override
    public List<WalletResponse> listWalletsCustomerByToken() {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return walletRepository.findByCustomer(customer).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a {@link Wallet} entity to a {@link WalletResponse} DTO.
     *
     * @param wallet the wallet entity
     * @return the response DTO
     */
    private WalletResponse mapToResponse(Wallet wallet) {
        WalletResponse response = new WalletResponse();
        response.setId(wallet.getId());
        response.setWalletName(wallet.getWalletName());
        response.setCurrency(wallet.getCurrency());
        response.setActiveForShopping(wallet.isActiveForShopping());
        response.setActiveForWithdraw(wallet.isActiveForWithdraw());
        response.setBalance(wallet.getBalance());
        response.setUsableBalance(wallet.getUsableBalance());
        return response;
    }

    /**
     * Retrieves all wallets in the system grouped by customer.
     * <p>
     * Typically used by EMPLOYEE users to view all customers and their associated wallets.
     *
     * @return list of {@link CustomerWithWalletsResponse} where each entry contains customer info and their wallets
     */
    @Override
    public List<CustomerWithWalletsResponse> listAllWalletsGroupedByCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerWithWalletsResponse> result = new ArrayList<>();

        for (Customer customer : customers) {
            List<Wallet> wallets = walletRepository.findByCustomer(customer);
            List<WalletResponse> walletResponses = wallets.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            CustomerWithWalletsResponse dto = new CustomerWithWalletsResponse();
            dto.setCustomerId(customer.getId());
            dto.setName(customer.getName());
            dto.setSurname(customer.getSurname());
            dto.setTckn(customer.getTckn());
            dto.setWallets(walletResponses);

            result.add(dto);
        }

        return result;
    }
}