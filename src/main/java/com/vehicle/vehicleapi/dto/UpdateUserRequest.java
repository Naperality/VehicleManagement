package com.vehicle.vehicleapi.dto;

import lombok.*;

import com.vehicle.vehicleapi.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Information Update Request")
@Data
@NoArgsConstructor
public class UpdateUserRequest {

    @Schema(example = "name123")
    private String username;

    @Schema(example = "example@email.com")
    private String email;

    @Schema(example = "USER")
    private Role role;
}
