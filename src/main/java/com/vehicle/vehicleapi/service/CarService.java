package com.vehicle.vehicleapi.service;

import com.vehicle.vehicleapi.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final List<Car> cars = new ArrayList<>();

    public List<Car> getAllCars(){
        return cars;
    }

    public Car getCarByTicket(int ticket){
        return cars.stream()
                .filter(car -> car.getTicket() == ticket)
                .findFirst()
                .orElse(null);
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public boolean deleteCar(int ticket){
        return cars.removeIf(car -> car.getTicket() == ticket);
    }

    public boolean updateCar(int ticket, Car car){
        Car existing = getCarByTicket(ticket);
        if(existing == null){
            return false;
        }
        existing.setBrand(car.getBrand());
        existing.setLicensePlate(car.getLicensePlate());
        existing.setColor(car.getColor());
        existing.setFuelType(car.getFuelType());
        existing.setModel(car.getModel());

        return true;
    }
}
