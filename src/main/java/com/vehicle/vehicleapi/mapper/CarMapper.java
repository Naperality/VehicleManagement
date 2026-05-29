package com.vehicle.vehicleapi.mapper;

import org.springframework.stereotype.Component;

import com.vehicle.vehicleapi.dto.CarResponse;
import com.vehicle.vehicleapi.model.Car;

@Component
public class CarMapper {

    public CarResponse toResponse(
            Car car
    ){
        return new CarResponse(
                car.getTicket(),
                car.getLicensePlate(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getFuelType()
        );
    }
}
