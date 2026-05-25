package com.vehicle.vehicleapi.exception;

public class CarNotFoundException extends RuntimeException{
    
    public CarNotFoundException (String message){
        super(message);
    }
}
