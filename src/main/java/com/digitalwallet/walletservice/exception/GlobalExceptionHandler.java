package com.digitalwallet.walletservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all controller exceptions.
 * Handles specific and general exceptions thrown by the application and returns standardized responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles CustomerNotFoundException with 404 Not Found status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Object> handleCustomerNotFound(CustomerNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles InvalidRoleException with 403 Forbidden status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> handleInvalidRole(InvalidRoleException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles AccessDeniedCustomException with 403 Forbidden status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedCustomException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles MissingParameterException with 400 Bad Request status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<Object> handleMissingParameter(MissingParameterException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic RuntimeException with 500 Internal Server Error status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        return buildResponse("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles WalletAlreadyExistsException with 400 Bad Request status.
     *
     * @param ex the exception
     * @return simple error map
     */
    @ExceptionHandler(WalletAlreadyExistsException.class)
    public ResponseEntity<?> handleWalletAlreadyExists(WalletAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", "WalletAlreadyExists",
                "message", ex.getMessage()
        ));
    }

    /**
     * Handles WalletNotFoundException with 404 Not Found status.
     *
     * @param ex the exception
     * @return structured error response
     */
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Object> handleWalletNotFoundException(WalletNotFoundException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "WalletNotFoundException");
        errorBody.put("message", ex.getMessage());
        errorBody.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors triggered by @Valid annotation.
     *
     * @param ex the exception containing field validation errors
     * @return structured error map for all invalid fields
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Error");
        response.put("message", errors);
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to build a consistent error response structure.
     *
     * @param message error message
     * @param status  HTTP status
     * @return structured response entity
     */
    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);

        return new ResponseEntity<>(errorBody, status);
    }
}