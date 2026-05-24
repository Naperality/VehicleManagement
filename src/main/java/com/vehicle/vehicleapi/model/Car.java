package com.vehicle.vehicleapi.model;

import jakarta.validation.constraints.NotBlank;

public class Car {
    private int ticket;
    @NotBlank(message = "License Plate is Required!")
    private String licensePlate;
    @NotBlank(message = "Brand is Required!")
    private String brand;
    @NotBlank(message = "Model is Required!")
    private String model;
    private String color;
    private String fuelType;

    public Car(
        int ticket,
        String licensePlate,
        String brand,
        String model,
        String color,
        String fuelType
    ){
        this.ticket = ticket;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.fuelType = fuelType;
    }

    // getters
    public int getTicket(){
        return ticket;
    }
    public String getLicensePlate(){
        return licensePlate;
    }
    public String getModel(){
        return model;
    }
    public String getBrand(){
        return brand;
    }
    public String getColor(){
        return color;
    }
    public String getFuelType(){
        return fuelType;
    }

    // setters
    public void setTicket(int ticket){
        this.ticket = ticket;
    }
    public void setLicensePlate(String licensePlate){
        this.licensePlate = licensePlate;
    }
    public void setModel(String model){
        this.model = model;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    public void setColor(String color){
        this.color = color;
    }
    public void setFuelType(String fuelType){
        this.fuelType = fuelType;
    }
}
