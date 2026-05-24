package com.vehicle.vehicleapi.service;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.CarRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository){
        this.repository = repository;
    }

    public List<Car> getAllCars(){
        return repository.findAll();
    }

    public Car getCarByTicket(int ticket){
        return repository.findAll()
                .stream()
                .filter(car -> car.getTicket() == ticket)
                .findFirst()
                .orElse(null);
    }

    public void addCar(Car car){
        repository.saveCar(car);
    }

    public boolean deleteCar(int ticket){
        return repository.deleteByTicket(ticket);
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
