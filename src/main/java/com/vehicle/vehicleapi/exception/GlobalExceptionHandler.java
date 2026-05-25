package com.vehicle.vehicleapi.exception;

import com.vehicle.vehicleapi.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>>
    handleCarNotFound(
            CarNotFoundException ex
    ){

        ApiResponse<Void> response =
                new ApiResponse<>(
                        false,
                        ex.getMessage(),
                        null
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ApiResponse<Object>>
    handleValidationErrors(
            MethodArgumentNotValidException ex
    ){

        String errorMessage =
                ex.getBindingResult()
                        .getFieldErrors()
                        .get(0)
                        .getDefaultMessage();

        ApiResponse<Object> response =
                new ApiResponse<>(
                        false,
                        errorMessage,
                        null
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}