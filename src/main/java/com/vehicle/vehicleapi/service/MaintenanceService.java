package com.vehicle.vehicleapi.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.dto.CreateMaintenanceRequest;
import com.vehicle.vehicleapi.dto.MaintenanceResponse;
import com.vehicle.vehicleapi.exception.CarNotFoundException;
import com.vehicle.vehicleapi.exception.RecordsNotFoundException;
import com.vehicle.vehicleapi.exception.UnauthorizedAccessException;
import com.vehicle.vehicleapi.mapper.MaintenanceMapper;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.model.MaintenanceRecord;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.repository.CarRepository;
import com.vehicle.vehicleapi.repository.RecordRepository;

@Service
public class MaintenanceService {

    private final RecordRepository repository;
    private final CarRepository carRepository;
    private final UserService userService;
    private final MaintenanceMapper mapper;

    public MaintenanceService(
        RecordRepository repository,
        CarRepository carRepository,
        UserService userService,
        MaintenanceMapper mapper
    ){
        this.repository = repository;
        this.carRepository = carRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    // adding record 
    public void createRecord(
        Long id, 
        CreateMaintenanceRequest request
    ){
        Car car = carRepository
                .findById(id)
                .orElseThrow(
                    () -> new CarNotFoundException(
                        "Vehicle Not Found!"
                    )
                );
        User user = userService.getCurrentUser();
        if(!car.getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException(
                "Vehicle does not belong to user!"
            );
        }

        if(
            request.getMaintenanceDate()
                .isAfter(LocalDate.now())
        ){
            throw new IllegalArgumentException(
                "Maintenance date cannot be in the future"
            );
        }

        MaintenanceRecord record = new MaintenanceRecord();

        record.setServiceType(
            request.getServiceType()
        );

        record.setDescription(
            request.getDescription()
        );

        record.setMaintenanceDate(
            request.getMaintenanceDate()
        );

        record.setMileage(
            request.getMileage()
        );

        record.setCost(
            request.getCost()
        );

        record.setStatus(
            request.getStatus()
        );

        record.setCar(car);

        repository.save(record);
    }

    // get records of current car
    public Page<MaintenanceResponse> getCarRecords(
        Long id,
        Pageable pageable
    ){
        Car car = carRepository
                .findById(id)
                .orElseThrow(
                    () -> new CarNotFoundException(
                        "Vehicle Not Found!"
                    )
                );
        User user = userService.getCurrentUser();
        if(!car.getUser().getId().equals(user.getId())){
            throw new UnauthorizedAccessException(
                "Vehicle does not belong to user!"
            );
        }
        Page<MaintenanceRecord> records = repository.findByCarTicket(id, pageable);
        
        if(records.isEmpty()){
            throw new RecordsNotFoundException(
                "No records found on this car!"
            );
        }
        

        return records.map(mapper::toResponse);
    }
    
}
