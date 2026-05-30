package com.vehicle.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
    description = "Token Information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String token;

}
