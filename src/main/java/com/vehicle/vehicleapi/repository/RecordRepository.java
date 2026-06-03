package com.vehicle.vehicleapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vehicle.vehicleapi.model.MaintenanceRecord;

public interface RecordRepository extends JpaRepository<MaintenanceRecord, Long>{
    
}
