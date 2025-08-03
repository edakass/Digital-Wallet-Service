package com.digitalwallet.walletservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up Spring Security.
 * <p>
 * Defines authentication and authorization mechanisms using JWT, stateless session management,
 * and endpoint access rules for CUSTOMER and EMPLOYEE roles.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs the {@code SecurityConfig} with required authentication components.
     *
     * @param jwtAuthFilter      the JWT authentication filter
     * @param userDetailsService the user details service for loading user credentials
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain for the application.
     * <p>
     * - Disables CSRF (not needed for token-based auth) <br>
     * - Permits access to Swagger UI and `/api/auth/**` endpoints <br>
     * - Secures wallet and transaction endpoints based on user roles <br>
     * - Enables stateless session management <br>
     * - Registers the {@link JwtAuthenticationFilter} before the default {@link UsernamePasswordAuthenticationFilter}
     *
     * @param http the {@link HttpSecurity} configuration object
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Wallet endpoints
                        .requestMatchers("/api/auth/wallets").hasAnyRole("CUSTOMER", "EMPLOYEE")
                        .requestMatchers("/api/auth/wallets/**").hasRole("EMPLOYEE")

                        // Transaction endpoints
                        .requestMatchers("/api/transactions/approve").hasRole("EMPLOYEE")
                        .requestMatchers("/api/transactions/**").hasAnyRole("CUSTOMER", "EMPLOYEE")

                        .anyRequest().authenticated()
                )

                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configures the authentication provider using DAO-based authentication.
     * <p>
     * Delegates to {@link CustomUserDetailsService} for loading user data,
     * and uses BCrypt for password encoding.
     *
     * @return the configured {@link AuthenticationProvider}
     */
    @SuppressWarnings("deprecation")
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Defines the {@link PasswordEncoder} bean using the BCrypt hashing algorithm.
     *
     * @return a {@link BCryptPasswordEncoder} instance
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}