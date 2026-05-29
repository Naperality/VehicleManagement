package com.vehicle.vehicleapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Information Request")
@Data
@NoArgsConstructor
public class CreateUserRequest {

    @Schema(example = "name123")
    @NotBlank(message = "Username is required!")
    private String username;

    @Schema(example = "example@email.com")
    @NotBlank(message = "Email is required!")
    @Email(message = "Incorrect Email Format!")
    private String email;

    @Schema(example = "WHbc_1284HFBcsa!")
    @NotBlank(message = "Password is required!")
    private String password;

    @Schema(example = "USER")
    @NotBlank(message = "Role is required!")
    private String role;
}
