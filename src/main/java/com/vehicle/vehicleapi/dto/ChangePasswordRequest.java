package com.vehicle.vehicleapi.dto;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Password Update Request")
@Data
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "Old Password is required!")
    @Schema(example = "Old Password")
    private String oldPassword;

    @NotBlank(message = "New Password is required!")
    @Schema(example = "New Password")
    private String newPassword;
}
