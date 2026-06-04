package com.vehicle.vehicleapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vehicle.vehicleapi.model.MaintenanceStatus;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {

    private Long id;

    private String serviceType;

    private String description;

    private LocalDate maintenanceDate;

    private Integer mileage;

    private BigDecimal cost;

    private MaintenanceStatus status;

    private Long carTicket;
}