package com.vehicle.vehicleapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCarRequest {

    private String brand;
    private String model;
    private String color;
    private String fuelType;
    private String licensePlate;
    
}
