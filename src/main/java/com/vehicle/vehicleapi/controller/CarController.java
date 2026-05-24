package com.vehicle.vehicleapi.controller;

import com.vehicle.vehicleapi.service.CarService;
import com.vehicle.vehicleapi.model.Car;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;

@RestController // Handles HTTP requests
@RequestMapping("/cars") // maps out the specified area
public class CarController {
    private final CarService service;

    // constructor as service
    public CarController(CarService service){
        this.service = service;
    }

    // get all cars in /cars
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars(){
        return ResponseEntity.ok(service.getAllCars());
    }

    //get specified car in /cars/{ticket}
    @GetMapping("/{ticket}")
    public ResponseEntity<Car> getCar(@PathVariable int ticket){
        Car car = service.getCarByTicket(ticket);
        if (car == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(car);
    }

    // post to /cars or add
    @PostMapping
    public ResponseEntity<String> addCar(@Valid@RequestBody Car car){
        service.addCar(car);
        return ResponseEntity.ok("Vehicle Added Successfully!");
    }

    // deletes car based on ticket in /cars/{ticket}
    @DeleteMapping("/{ticket}")
    public ResponseEntity<String> deleteCar(@PathVariable int ticket){
        boolean deleted = service.deleteCar(ticket);
        if (deleted){
            return ResponseEntity.ok("Vehicle Deleted Successfully!");
        }
        return ResponseEntity.notFound().build();
    }

    // update using put
    @PutMapping("/{ticket}")
    public ResponseEntity<String> updateCar(
        @PathVariable int ticket,
        @Valid@RequestBody Car updatedCar
    ){
        boolean updated = service.updateCar(ticket, updatedCar);

        if(updated){
            return ResponseEntity.ok("Vehicle Updated!");
        }
        return ResponseEntity.notFound().build();
    }
}
