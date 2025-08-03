package com.digitalwallet.walletservice.security;

import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Employee;
import com.digitalwallet.walletservice.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Service class for generating and validating JWT tokens for authenticated users.
 * <p>
 * Supports both {@link Customer} and {@link Employee} users with role-based claims.
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign and validate JWT tokens, injected from application properties.
     */
    @Value("${jwt.secret}")
    private String secretKeyString;

    /**
     * The parsed signing key derived from the secret key string.
     */
    private Key key;

    /**
     * Token expiration time in milliseconds (1 day).
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;


    /**
     * Initializes the {@link Key} object using the provided secret key after bean construction.
     */
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    /**
     * Generates a JWT token for a {@link Customer}.
     *
     * @param customer the authenticated customer
     * @return a signed JWT token string containing the customer's TCKN and role
     */
    public String generateToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getTckn())
                .claim("role", Role.CUSTOMER.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT token for an {@link Employee}.
     *
     * @param employee the authenticated employee
     * @return a signed JWT token string containing the employee's email and role
     */
    public String generateToken(Employee employee) {
        return Jwts.builder()
                .setSubject(employee.getEmail())
                .claim("role", Role.EMPLOYEE.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a token for the given {@link Customer}.
     * <p>
     * Checks that the username (TCKN) in the token matches and that the token is not expired.
     *
     * @param token    the JWT token string
     * @param customer the customer to validate against
     * @return {@code true} if the token is valid; {@code false} otherwise
     */
    public boolean isTokenValid(String token, Customer customer) {
        final String tckn = extractUsername(token);
        return tckn.equals(customer.getTckn()) && !isTokenExpired(token);
    }

    /**
     * Extracts the username (subject) from the given token.
     *
     * @param token the JWT token string
     * @return the subject (username, TCKN or email)
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the role from the given token.
     *
     * @param token the JWT token string
     * @return the user's role as a string
     */
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extracts a specific claim from the token using the provided resolver function.
     *
     * @param token           the JWT token string
     * @param claimsResolver  a function to extract a claim from the {@link Claims} object
     * @param <T>             the type of the extracted claim
     * @return the extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the token and returns the JWT claims.
     *
     * @param token the JWT token string
     * @return the decoded {@link Claims} object
     */
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks whether the token has expired.
     *
     * @param token the JWT token string
     * @return {@code true} if the token is expired; {@code false} otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}