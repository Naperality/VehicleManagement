package com.vehicle.vehicleapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serviceType;

    private String description;
    
    private LocalDate maintenanceDate;

    private Integer mileage;

    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @ManyToOne
    @JoinColumn(name = "car_ticket")
    private Car car;
}
