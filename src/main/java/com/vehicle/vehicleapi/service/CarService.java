package com.vehicle.vehicleapi.service;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.CarRepository;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.exception.CarNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository){
        this.repository = repository;
    }

    public List<Car> getAllCars(){
        return repository.findAll();// using JPA findAll or show all data in table or repo
    }

    public Car getCarByTicket(int ticket){
        // return repository.findAll()
        //         .stream()
        //         .filter(car -> car.getTicket() == ticket)
        //         .findFirst()
        //         .orElse(null); // this is for stream using fake repo in array list
        return repository.findById(ticket).orElseThrow(
            () -> new CarNotFoundException(
                "Vehicle not found with ticket: "+ticket
            )// using the exception throw
        );// using JPA postgresql find ticket
    }

    public void addCar(CreateCarRequest request){
        // ORM - Object relational Mapping
        Car car = new Car();

        car.setLicensePlate(request.getLicensePlate());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setColor(request.getColor());
        car.setFuelType(request.getFuelType());

        repository.save(car);// add car using JPA database postgresql
    }

    public void deleteCar(int ticket){
        if (!repository.existsById(ticket)){
            throw new CarNotFoundException(
                "Vehicle not found with ticket: "+ticket
            );// use the exception error made in /exception folder
        }
        repository.deleteById(ticket);// delete using JPA database postgresql
    }

    public void updateCar(int ticket, UpdateCarRequest request){
        Car existing = getCarByTicket(ticket);// --does not need the throw exception due to getCarByticket
        // Check if not null to update specifics only
        
        if(request.getBrand() != null){
            existing.setBrand(request.getBrand());
        }
        
        if(request.getLicensePlate() != null){
            existing.setLicensePlate(request.getLicensePlate());
        }

        if(request.getColor() != null){
            existing.setColor(request.getColor());
        }
        
        if(request.getFuelType() != null){
            existing.setFuelType(request.getFuelType());
        }
        
        if(request.getModel() != null){
            existing.setModel(request.getModel());
        }
        
        repository.save(existing);
    }
}
