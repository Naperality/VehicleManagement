package com.vehicle.vehicleapi.exception;

public class CarAlreadyExistException extends RuntimeException{

    public CarAlreadyExistException(String message){
        super(message);
    }
    
}
