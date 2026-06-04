package com.vehicle.vehicleapi.exception;

public class UnauthorizedAccessException extends RuntimeException{
    
    public UnauthorizedAccessException(String message){
        super(message);
    }
}
