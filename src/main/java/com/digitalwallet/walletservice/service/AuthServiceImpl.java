package com.digitalwallet.walletservice.service;


import com.digitalwallet.walletservice.dto.AuthRequest;
import com.digitalwallet.walletservice.dto.AuthResponse;
import com.digitalwallet.walletservice.dto.RegisterRequest;
import com.digitalwallet.walletservice.enums.Role;
import com.digitalwallet.walletservice.model.Customer;
import com.digitalwallet.walletservice.model.Employee;
import com.digitalwallet.walletservice.repository.CustomerRepository;
import com.digitalwallet.walletservice.repository.EmployeeRepository;
import com.digitalwallet.walletservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthService} interface that handles
 * user registration and login functionality for both customers and employees.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Constructs an instance of {@code AuthServiceImpl} with required dependencies.
     *
     * @param customerRepository repository for customer persistence operations
     * @param employeeRepository repository for employee persistence operations
     * @param passwordEncoder    utility for password hashing and verification
     * @param jwtService         service for JWT token generation and validation
     */
    public AuthServiceImpl(CustomerRepository customerRepository,
                           EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    /**
     * Registers a new user based on the provided role in the request.
     * <p>
     * - If role is CUSTOMER: requires {@code tckn}, saves the user in the customer repository.<br>
     * - If role is EMPLOYEE: requires {@code email}, saves the user in the employee repository.<br>
     * Generates and returns a JWT token upon successful registration.
     *
     * @param request the registration request containing user details
     * @return {@link AuthResponse} containing the generated JWT token
     * @throws IllegalArgumentException if required fields are missing or role is invalid
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (request.getRole() == Role.CUSTOMER) {
            if (request.getTckn() == null || request.getTckn().isBlank()) {
                throw new IllegalArgumentException("TCKN is required for CUSTOMER");
            }

            Customer customer = new Customer();
            customer.setName(request.getName());
            customer.setSurname(request.getSurname());
            customer.setTckn(request.getTckn());
            customer.setPassword(encodedPassword);
            customerRepository.save(customer);

            String token = jwtService.generateToken(customer);
            return new AuthResponse(token);

        } else if (request.getRole() == Role.EMPLOYEE) {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email is required for EMPLOYEE");
            }

            Employee employee = new Employee();
            employee.setName(request.getName());
            employee.setSurname(request.getSurname());
            employee.setEmail(request.getEmail());
            employee.setPassword(encodedPassword);
            employeeRepository.save(employee);

            String token = jwtService.generateToken(employee);
            return new AuthResponse(token);
        }

        throw new IllegalArgumentException("Invalid role: must be CUSTOMER or EMPLOYEE");
    }


    /**
     * Authenticates a user and returns a JWT token if the provided credentials are valid.
     * <p>
     * - CUSTOMER: looks up user by {@code tckn} and validates the password.<br>
     * - EMPLOYEE: looks up user by {@code email} and validates the password.
     *
     * @param request the authentication request containing role, credentials, and password
     * @return {@link AuthResponse} containing a JWT token upon successful authentication
     * @throws IllegalArgumentException if the credentials are invalid or role is unrecognized
     */
    @Override
    public AuthResponse login(AuthRequest request) {
        if ("CUSTOMER".equalsIgnoreCase(request.getRole())) {
            return customerRepository.findByTckn(request.getTckn())
                    .filter(c -> passwordEncoder.matches(request.getPassword(), c.getPassword()))
                    .map(c -> new AuthResponse(jwtService.generateToken(c)))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        } else if ("EMPLOYEE".equalsIgnoreCase(request.getRole())) {
            return employeeRepository.findByEmail(request.getEmail())
                    .filter(e -> passwordEncoder.matches(request.getPassword(), e.getPassword()))
                    .map(e -> new AuthResponse(jwtService.generateToken(e)))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        }

        throw new IllegalArgumentException("Invalid role: must be CUSTOMER or EMPLOYEE");
    }
}