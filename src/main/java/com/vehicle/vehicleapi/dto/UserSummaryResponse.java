package com.vehicle.vehicleapi.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Information Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponse {
    
    @Schema(example = "1!")
    private Long id;

    @Schema(example = "name123")
    private String username;

    @Schema(example = "example@email.com")
    private String email;

    @Schema(example = "USER")
    private String role;

    @Schema(example = "10")
    private int carCount;

}
