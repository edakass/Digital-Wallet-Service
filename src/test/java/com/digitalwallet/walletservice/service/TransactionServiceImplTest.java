package com.digitalwallet.walletservice.service;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.digitalwallet.walletservice.dto.DepositRequest;
import com.digitalwallet.walletservice.dto.TransactionApprovalRequest;
import com.digitalwallet.walletservice.dto.TransactionResponse;
import com.digitalwallet.walletservice.dto.WithDrawRequest;
import com.digitalwallet.walletservice.enums.OppositePartyType;
import com.digitalwallet.walletservice.enums.TransactionStatus;
import com.digitalwallet.walletservice.enums.TransactionType;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Transaction;
import com.digitalwallet.walletservice.model.Wallet;
import com.digitalwallet.walletservice.repository.TransactionRepository;
import com.digitalwallet.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Customer customer;

    private Wallet wallet;

    @Mock
    private Authentication auth;

    @Mock
    private SecurityContext context;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        wallet = new Wallet();
        wallet.setId(100L);
        wallet.setCustomer(customer);
        wallet.setBalance(BigDecimal.valueOf(2000));
        wallet.setUsableBalance(BigDecimal.valueOf(1500));
        wallet.setActiveForWithdraw(true);

        GrantedAuthority authority = () -> "ROLE_CUSTOMER";
        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        auth = mock(Authentication.class);
        context = mock(SecurityContext.class);

        when(auth.getPrincipal()).thenReturn(customer);
        doReturn(authorities).when(auth).getAuthorities();

        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }


    @Test
    void testDeposit_ApprovedAmount() {
        DepositRequest request = new DepositRequest(100L, BigDecimal.valueOf(500), "TR111",
                OppositePartyType.IBAN);

        when(walletRepository.findById(100L)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        TransactionResponse response = transactionService.deposit(request);

        assertEquals(BigDecimal.valueOf(2500), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(2000), wallet.getUsableBalance());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
    }

    @Test
    void testDeposit_PendingAmount() {
        DepositRequest request = new DepositRequest(100L, BigDecimal.valueOf(1500),
                "TR111", OppositePartyType.IBAN);

        when(walletRepository.findById(100L)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(2L);
            return t;
        });

        TransactionResponse response = transactionService.deposit(request);

        assertEquals(BigDecimal.valueOf(3500), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(1500), wallet.getUsableBalance());
        assertEquals(TransactionStatus.PENDING, response.getStatus());
    }

    @Test
    void testWithdraw_Approved() {
        WithDrawRequest request = new WithDrawRequest(100L, BigDecimal.valueOf(200), OppositePartyType.IBAN,
                "TR222");

        when(walletRepository.findById(100L)).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        TransactionResponse response = transactionService.withdraw(request);

        assertEquals(BigDecimal.valueOf(1800), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(1300), wallet.getUsableBalance());
        assertEquals(TransactionStatus.APPROVED, response.getStatus());
    }

    @Test
    void testWithdraw_InsufficientBalance() {
        WithDrawRequest request = new WithDrawRequest(100L, BigDecimal.valueOf(5000),
                OppositePartyType.IBAN, "TR222");

        when(walletRepository.findById(100L)).thenReturn(Optional.of(wallet));

        assertThrows(IllegalArgumentException.class, () -> transactionService.withdraw(request));
    }


    @Test
    void testApproveTransaction_Deposit() {
        Transaction transaction = new Transaction();
        transaction.setId(3L);
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setWallet(wallet);

        TransactionApprovalRequest request = new TransactionApprovalRequest(3L, TransactionStatus.APPROVED);

        when(transactionRepository.findById(3L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        TransactionResponse response = transactionService.approveTransaction(request);

        assertEquals(TransactionStatus.APPROVED, response.getStatus());
        assertEquals(BigDecimal.valueOf(2500), wallet.getUsableBalance());
    }

    @Test
    void testApproveTransaction_AlreadyApproved() {
        Transaction transaction = new Transaction();
        transaction.setId(4L);
        transaction.setStatus(TransactionStatus.APPROVED);

        TransactionApprovalRequest request = new TransactionApprovalRequest(4L, TransactionStatus.APPROVED);

        when(transactionRepository.findById(4L)).thenReturn(Optional.of(transaction));

        assertThrows(IllegalStateException.class, () -> transactionService.approveTransaction(request));
    }

    @Test
    void testGetTransactionsForWallet() {
        Transaction txn = new Transaction();
        txn.setId(10L);
        txn.setWallet(wallet);
        txn.setAmount(BigDecimal.valueOf(500));
        txn.setType(TransactionType.DEPOSIT);
        txn.setStatus(TransactionStatus.APPROVED);

        when(walletRepository.findById(100L)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByWallet(wallet)).thenReturn(List.of(txn));

        List<TransactionResponse> responses = transactionService.getTransactionsForWallet(100L);

        assertEquals(1, responses.size());
        assertEquals(BigDecimal.valueOf(500), responses.get(0).getAmount());
    }

    @Test
    void testAuthorizeWalletAccess_Forbidden() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setId(2L);
        Wallet otherWallet = new Wallet();
        otherWallet.setId(200L);
        otherWallet.setCustomer(anotherCustomer);

        when(walletRepository.findById(200L)).thenReturn(Optional.of(otherWallet));

        assertThrows(AccessDeniedException.class,
                () -> transactionService.getTransactionsForWallet(200L));
    }
}