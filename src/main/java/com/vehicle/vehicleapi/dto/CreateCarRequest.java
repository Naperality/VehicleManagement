package com.vehicle.vehicleapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor

public class CreateCarRequest {
    @NotBlank(message = "License Plate is Required!")
    private String licensePlate;
    @NotBlank(message = "Brand is Required!")
    private String brand;
    @NotBlank(message = "Model is Required!")
    private String model;
    @NotBlank(message = "Color is Required!")
    private String color;
    @NotBlank(message = "Fuel Type is Required!")
    private String fuelType;

    // public CreateCarRequest() {}
    // public String getLicensePlate() {
    //     return licensePlate;
    // }
    // public void setLicensePlate(String licensePlate) {
    //     this.licensePlate = licensePlate;
    // }
    // public String getBrand() {
    //     return brand;
    // }
    // public void setBrand(String brand) {
    //     this.brand = brand;
    // }
    // public String getModel() {
    //     return model;
    // }
    // public void setModel(String model) {
    //     this.model = model;
    // }
    // public String getColor() {
    //     return color;
    // }
    // public void setColor(String color) {
    //     this.color = color;
    // }
    // public String getFuelType() {
    //     return fuelType;
    // }
    // public void setFuelType(String fuelType) {
    //     this.fuelType = fuelType;
    // }
}
