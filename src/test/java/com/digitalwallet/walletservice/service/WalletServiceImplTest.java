package com.digitalwallet.walletservice.service;

import com.digitalwallet.walletservice.dto.CreateWalletRequest;
import com.digitalwallet.walletservice.dto.CustomerWithWalletsResponse;
import com.digitalwallet.walletservice.dto.WalletResponse;
import com.digitalwallet.walletservice.enums.Currency;
import com.digitalwallet.walletservice.exception.WalletAlreadyExistsException;
import com.digitalwallet.walletservice.exception.WalletNotFoundException;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Wallet;
import com.digitalwallet.walletservice.repository.CustomerRepository;
import com.digitalwallet.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleCustomer = new Customer();
        sampleCustomer.setId(1L);
        sampleCustomer.setName("Ali");
        sampleCustomer.setSurname("Yilmaz");
        sampleCustomer.setTckn("19845678901");
    }

    @Test
    void testCreateWallet_Success() {
        CreateWalletRequest request = new CreateWalletRequest("My Wallet", Currency.USD,
                true, true);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(walletRepository.existsByCustomerIdAndCurrency(1L, Currency.USD)).thenReturn(false);
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> {
            Wallet wallet = invocation.getArgument(0);
            wallet.setId(100L);
            return wallet;
        });

        WalletResponse response = walletService.createWallet(request, 1L);

        assertEquals("My Wallet", response.getWalletName());
        assertEquals(Currency.USD, response.getCurrency());
        assertEquals(BigDecimal.ZERO, response.getBalance());
    }

    @Test
    void testCreateWallet_CustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        CreateWalletRequest request = new CreateWalletRequest("Wallet", Currency.TRY,
                true, true);

        assertThrows(IllegalArgumentException.class,
                () -> walletService.createWallet(request, 99L));
    }

    @Test
    void testCreateWallet_AlreadyExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(walletRepository.existsByCustomerIdAndCurrency(1L, Currency.EUR)).thenReturn(true);

        CreateWalletRequest request = new CreateWalletRequest("Existing", Currency.EUR,
                true, false);

        assertThrows(WalletAlreadyExistsException.class,
                () -> walletService.createWallet(request, 1L));
    }

    @Test
    void testListWalletsCustomer_Success() {
        Wallet wallet = new Wallet();
        wallet.setId(1L);
        wallet.setWalletName("Test");
        wallet.setCurrency(Currency.USD);
        wallet.setBalance(BigDecimal.TEN);
        wallet.setUsableBalance(BigDecimal.TEN);
        wallet.setCustomer(sampleCustomer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(walletRepository.findByCustomer(sampleCustomer)).thenReturn(List.of(wallet));

        List<WalletResponse> result = walletService.listWalletsCustomer(1L);

        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getWalletName());
    }

    @Test
    void testListWalletsCustomer_NoWallets() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
        when(walletRepository.findByCustomer(sampleCustomer)).thenReturn(Collections.emptyList());

        assertThrows(WalletNotFoundException.class,
                () -> walletService.listWalletsCustomer(1L));
    }

    @Test
    void testListWalletsCustomerByToken() {
        Wallet wallet = new Wallet();
        wallet.setId(2L);
        wallet.setWalletName("Ali Wallet");
        wallet.setCurrency(Currency.TRY);
        wallet.setBalance(BigDecimal.ONE);
        wallet.setCustomer(sampleCustomer);

        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(sampleCustomer);
        SecurityContextHolder.setContext(context);

        when(walletRepository.findByCustomer(sampleCustomer)).thenReturn(List.of(wallet));

        List<WalletResponse> result = walletService.listWalletsCustomerByToken();

        assertEquals(1, result.size());
        assertEquals("Ali Wallet", result.get(0).getWalletName());
    }

    @Test
    void testListAllWalletsGroupedByCustomer() {
        Wallet wallet = new Wallet();
        wallet.setId(3L);
        wallet.setWalletName("Group Wallet");
        wallet.setCurrency(Currency.EUR);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCustomer(sampleCustomer);

        when(customerRepository.findAll()).thenReturn(List.of(sampleCustomer));
        when(walletRepository.findByCustomer(sampleCustomer)).thenReturn(List.of(wallet));

        List<CustomerWithWalletsResponse> result = walletService.listAllWalletsGroupedByCustomer();

        assertEquals(1, result.size());
        assertEquals("Ali", result.get(0).getName());
        assertEquals(1, result.get(0).getWallets().size());
    }
}