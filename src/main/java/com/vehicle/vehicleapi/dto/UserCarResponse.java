package com.vehicle.vehicleapi.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Car Information Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCarResponse {
    
    @Schema(example = "1")
    private Long ticket;

    @Schema(example = "ABC123")
    private String licensePlate;

}
