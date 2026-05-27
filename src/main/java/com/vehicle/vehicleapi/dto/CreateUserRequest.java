package com.vehicle.vehicleapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "Username is required!")
    private String username;
    @NotBlank(message = "Email is required!")
    @Email(message = "Incorrect Email Format!")
    private String email;
    @NotBlank(message = "Username is required!")
    private String password;
    @NotBlank(message = "Username is required!")
    private String role;
}
