package com.vehicle.vehicleapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vehicle.vehicleapi.model.MaintenanceRecord;

public interface RecordRepository extends JpaRepository<MaintenanceRecord, Long>{
    Page<MaintenanceRecord> findByCarTicket(Long ticket, Pageable pageable);
    Page<MaintenanceRecord> findByCarUserId(Long userId, Pageable pageable);
}
