package com.digitalwallet.walletservice.security;

import com.digitalwallet.walletservice.enums.Role;
import com.digitalwallet.walletservice.repository.EmployeeRepository;
import com.digitalwallet.walletservice.repository.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Filter that intercepts incoming HTTP requests to perform JWT-based authentication.
 * <p>
 * Extracts the JWT token from the Authorization header, validates it,
 * and sets the authentication in the {@link SecurityContextHolder}
 * for both CUSTOMER and EMPLOYEE roles.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Constructs a {@code JwtAuthenticationFilter} with required dependencies.
     *
     * @param jwtService         service to parse and validate JWT tokens
     * @param customerRepository repository to fetch CUSTOMER entities
     * @param employeeRepository repository to fetch EMPLOYEE entities
     */
    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomerRepository customerRepository,
                                   EmployeeRepository employeeRepository) {
        this.jwtService = jwtService;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Filters each HTTP request to extract and validate the JWT token.
     * <p>
     * If a valid token is found and the user is not yet authenticated,
     * the corresponding CUSTOMER or EMPLOYEE is loaded and authenticated in the security context.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Proceed if Authorization header is missing or doesn't contain a Bearer token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7); // Strip "Bearer "
        final String username = jwtService.extractUsername(token);
        final String roleStr = jwtService.extractRole(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Role role = Role.valueOf(roleStr);

            if (role == Role.CUSTOMER) {
                customerRepository.findByTckn(username).ifPresent(customer -> {
                    if (jwtService.isTokenValid(token, customer)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        customer,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            } else if (role == Role.EMPLOYEE) {
                employeeRepository.findByEmail(username).ifPresent(employee -> {
                    if (!jwtService.isTokenExpired(token)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        employee,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        }

        filterChain.doFilter(request, response);
    }
}