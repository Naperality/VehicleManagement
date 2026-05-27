package com.vehicle.vehicleapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {

    private Long ticket;
    private String licensePlate;
    private String brand;
    private String model;
    private String color;
    private String fuelType;
    
}
