package com.vehicle.vehicleapi.mapper;

import org.springframework.stereotype.Component;

import com.vehicle.vehicleapi.dto.MaintenanceResponse;
import com.vehicle.vehicleapi.model.MaintenanceRecord;

@Component
public class MaintenanceMapper {

    public MaintenanceResponse toResponse(
        MaintenanceRecord record
    ){
        return new MaintenanceResponse(
            record.getId(),
            record.getServiceType(),
            record.getDescription(),
            record.getMaintenanceDate(),
            record.getMileage(),
            record.getCost(),
            record.getStatus(),
            record.getCar().getTicket()
        );
    }
}
