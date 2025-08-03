package com.digitalwallet.walletservice.service;

import com.digitalwallet.walletservice.dto.AuthRequest;
import com.digitalwallet.walletservice.dto.AuthResponse;
import com.digitalwallet.walletservice.dto.RegisterRequest;

/**
 * Service interface for handling authentication operations.
 */
public interface AuthService {

    /**
     * Registers a new user (Customer or Employee) based on the provided request.
     *
     * @param request the authentication request containing registration details
     * @return an AuthResponse containing the generated JWT token
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a user and returns a JWT token if the credentials are valid.
     *
     * @param request the authentication request containing login credentials
     * @return an AuthResponse containing the generated JWT token
     */
    AuthResponse login(AuthRequest request);
}