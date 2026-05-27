package com.vehicle.vehicleapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String role;
}
