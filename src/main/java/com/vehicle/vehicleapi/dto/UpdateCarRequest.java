package com.vehicle.vehicleapi.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Car Information Update Request")
@Data
@NoArgsConstructor
public class UpdateCarRequest {

    @Schema(example = "ABC123")
    private String licensePlate;

    @Schema(example = "Toyota")
    private String brand;

    @Schema(example = "Civic")
    private String model;

    @Schema(example = "Black")
    private String color;

    @Schema(example = "Gasoline")
    private String fuelType;

    // public UpdateCarRequest() {}// empty contructor for JSON Deserialization

    // // setters and getters
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
