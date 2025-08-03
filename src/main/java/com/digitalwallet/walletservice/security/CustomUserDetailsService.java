package com.digitalwallet.walletservice.security;

import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom implementation of {@link UserDetailsService} for Spring Security.
 * Loads user-specific data based on the customer's TCKN.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    /**
     * Constructor for injecting the CustomerRepository dependency.
     *
     * @param customerRepository the repository to access Customer data
     */
    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Loads the {@link UserDetails} by username (TCKN in this case).
     *
     * @param username the TCKN of the customer
     * @return UserDetails object containing the customer's authentication info
     * @throws UsernameNotFoundException if the customer is not found by the given TCKN
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByTckn(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));

        return new org.springframework.security.core.userdetails.User(
                customer.getTckn(),
                customer.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
        );
    }
}