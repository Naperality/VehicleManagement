package com.vehicle.vehicleapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Car Information Request")
@Data
@NoArgsConstructor
public class CreateCarRequest {

    @Schema(example = "ABC123")
    @NotBlank(message = "License Plate is Required!")
    private String licensePlate;

    @Schema(example = "Toyota")
    @NotBlank(message = "Brand is Required!")
    private String brand;

    @Schema(example = "Civic")
    @NotBlank(message = "Model is Required!")
    private String model;

    @Schema(example = "Black")
    @NotBlank(message = "Color is Required!")
    private String color;

    @Schema(example = "Gasoline")
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
