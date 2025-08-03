package com.digitalwallet.walletservice.controller;

import com.digitalwallet.walletservice.dto.AuthRequest;
import com.digitalwallet.walletservice.dto.AuthResponse;
import com.digitalwallet.walletservice.dto.RegisterRequest;
import com.digitalwallet.walletservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller class for handling authentication-related operations.
 * Provides endpoints for user registration and login for both CUSTOMER and EMPLOYEE roles.
 * <p>
 * Base path: /api/auth
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor-based dependency injection for AuthService.
     *
     * @param authService the authentication service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to register a new user (CUSTOMER or EMPLOYEE).
     *
     * @param request the request object containing user registration data
     * @return the response containing the generated JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint to authenticate a user and generate a JWT token.
     *
     * @param request the request object containing login credentials
     * @return the response containing the generated JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}