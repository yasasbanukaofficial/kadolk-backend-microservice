package com.spms.vehicleservice.exceptions;

import com.spms.vehicleservice.util.ApiResponse;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehicleNotFound(VehicleNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VehicleNumberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehicleNumberAlreadyExists(VehicleNumberAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VehicleAlreadyInsideException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehicleAlreadyInside(VehicleAlreadyInsideException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VehicleNotInsideException.class)
    public ResponseEntity<ApiResponse<Void>> handleVehicleNotInside(VehicleNotInsideException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
            .map(violation -> violation.getMessage())
            .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors)
        );
    }

    @ExceptionHandler({
        OptimisticLockException.class,
        PessimisticLockException.class,
        OptimisticLockingFailureException.class,
        PessimisticLockingFailureException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleLockConflict(RuntimeException ex) {
        return build(HttpStatus.CONFLICT, "Concurrent modification detected. Please try again.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status.value(), message, null));
    }
}
