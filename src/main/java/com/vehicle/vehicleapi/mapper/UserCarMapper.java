package com.vehicle.vehicleapi.mapper;

import org.springframework.stereotype.Component;

import com.vehicle.vehicleapi.dto.UserCarResponse;
import com.vehicle.vehicleapi.model.Car;

@Component
public class UserCarMapper {

    public UserCarResponse toResponse(Car car){

        return new UserCarResponse(
            car.getTicket(),
            car.getLicensePlate()
        );
    }
    
}
