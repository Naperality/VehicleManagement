package com.vehicle.vehicleapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vehicle.vehicleapi.model.MaintenanceStatus;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaintenanceRequest {

    private String serviceType;

    private String description;

    private LocalDate maintenanceDate;

    @Positive
    private Integer mileage;

    @Positive
    private BigDecimal cost;

    private MaintenanceStatus status;
}