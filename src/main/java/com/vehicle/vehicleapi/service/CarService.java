package com.vehicle.vehicleapi.service;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.CarRepository;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.exception.CarNotFoundException;
import com.vehicle.vehicleapi.exception.CarAlreadyExistException;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
// import org.springframework.web.servlet.FlashMap;

import java.util.List;

@Service
public class CarService {
    private final CarRepository repository;

    public CarService(CarRepository repository){
        this.repository = repository;
    }

    public Page<Car> getAllCars(Pageable pageable){
        return repository.findAll(pageable);// using JPA findAll or show all data in table or repo
    }

    public Car getCarByTicket(Long ticket){
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
        if(repository.findByLicensePlate(
                    request.getLicensePlate()
                ).isPresent()){
                throw new CarAlreadyExistException(
                        "Car already exists with plate: "
                        + request.getLicensePlate()
                );
            }
        Car car = new Car();
        car.setLicensePlate(request.getLicensePlate());
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setColor(request.getColor());
        car.setFuelType(request.getFuelType());

        repository.save(car);// add car using JPA database postgresql
    }

    public void deleteCar(Long ticket){
        if (!repository.existsById(ticket)){
            throw new CarNotFoundException(
                "Vehicle not found with ticket: "+ticket
            );// use the exception error made in /exception folder
        }
        repository.deleteById(ticket);// delete using JPA database postgresql
    }

    public void updateCar(Long ticket, UpdateCarRequest request){
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

    // using the own methods in repo
    public Car getByLicensePlate(String licensePlate){
        return repository
                .findByLicensePlate(licensePlate)
                .orElseThrow(
                    () -> new CarNotFoundException(
                        "Vehicle Not Found!"
                    )
                );
    }

    public Page<Car> getByBrand(String brand, Pageable pageable){
        Page<Car> cars = repository.findByBrand(brand, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with "+brand
            );
        }
        return cars;
    }

    public Page<Car> getByModel(String model, Pageable pageable){
        Page<Car> cars = repository.findByModel(model, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with "+model
            );
        }
        return cars;
    }

    public Page<Car> getByFuelType(String fuel, Pageable pageable){
        Page<Car> cars = repository.findByFuelType(fuel, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with "+fuel
            );
        }
        return cars;
    }

    public Page<Car> getByColor(String color, Pageable pageable){
        Page<Car> cars = repository.findByColor(color, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with "+color
            );
        }
        return cars;
    }

    public Page<Car> getByBrandAndColor(String brand, String color, Pageable pageable){
        Page<Car> cars = repository.findByBrandAndColor(brand,color, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with brand of "+brand+" and color of "+color
            );
        }
        return cars;
    }

    public Page<Car> getBrandContaining(String brand, Pageable pageable){
        Page<Car> cars = repository.findByBrandContaining(brand, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with brand of "+brand
            );
        }
        return cars;
    }

    public Page<Car> getBrandStartingWith(String brand, Pageable pageable){
        Page<Car> cars = repository.findByBrandStartingWith(brand, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with brand starting with "+brand
            );
        }
        return cars;
    }

    public Page<Car> getBrandEndingWith(String brand, Pageable pageable){
        Page<Car> cars = repository.findByBrandEndingWith(brand, pageable);
        if(cars.isEmpty()){
            throw new CarNotFoundException(
                "No Vehicles found with brand ending with "+brand
            );
        }
        return cars;
    }
}
