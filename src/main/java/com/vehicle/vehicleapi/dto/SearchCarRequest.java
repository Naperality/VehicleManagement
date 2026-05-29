package com.vehicle.vehicleapi.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Car Information Search Request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCarRequest {

    @Schema(example = "Toyota")
    private String brand;

    @Schema(example = "Civic")
    private String model;

    @Schema(example = "Black")
    private String color;

    @Schema(example = "Gasoline")
    private String fuelType;

    @Schema(example = "ABC123")
    private String licensePlate;
    
}
