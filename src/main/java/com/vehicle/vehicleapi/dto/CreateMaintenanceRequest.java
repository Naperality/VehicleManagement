package com.vehicle.vehicleapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vehicle.vehicleapi.model.MaintenanceStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Maintenance Request Body")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequest {

    @Schema(example = "Oil Change")
    @NotBlank
    private String serviceType;

    @Schema(example = "Change Oil and Filter")
    private String description;

    @Schema(example = "2026-06-03")
    @NotNull
    private LocalDate maintenanceDate;

    @Schema(example = "45000")
    @NotNull
    @PositiveOrZero
    private Integer mileage;

    @Schema(example = "2500")
    @NotNull
    @PositiveOrZero
    private BigDecimal cost;

    @Schema(example = "COMPLETED")
    @NotNull
    private MaintenanceStatus status;
}