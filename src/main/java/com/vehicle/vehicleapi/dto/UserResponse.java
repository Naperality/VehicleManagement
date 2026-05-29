package com.vehicle.vehicleapi.dto;

import java.util.List;

import com.vehicle.vehicleapi.model.Role;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Information Response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @Schema(example = "1!")
    private Long id;

    @Schema(example = "name123")
    private String username;

    @Schema(example = "example@email.com")
    private String email;

    @Schema(example = "USER")
    private Role role;

    @Schema(example = "10")
    private int carCount;

    @Schema(example = """
            [
                ["ticket": "1",
                "licensePlate": "ABC123"]
            ]
            """)
    private List<UserCarResponse> cars;
    
}