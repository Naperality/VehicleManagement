package com.vehicle.vehicleapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(
    description = "Log in Information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Schema(example = "Your Username")
    @NotBlank
    private String username;

    @Schema(example = "Your Password")
    @NotBlank
    private String password;
    
}
