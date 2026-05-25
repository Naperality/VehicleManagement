package com.vehicle.vehicleapi.controller;

import com.vehicle.vehicleapi.service.CarService;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.ApiResponse;

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
    public ResponseEntity<ApiResponse<List<Car>>> getAllCars(){
        ApiResponse<List<Car>> response =
                new ApiResponse<>(
                        true,
                        "Vehicles retrieved successfully",
                        service.getAllCars()
                );

        return ResponseEntity.ok(response);
    }

    //get specified car in /cars/{ticket}
    @GetMapping("/{ticket}")
    public ResponseEntity<ApiResponse<Car>> getCar(@PathVariable int ticket){
        Car car = service.getCarByTicket(ticket);
        // if (car == null){
        //     return ResponseEntity.notFound().build();
        // }
        // return ResponseEntity.ok(car); -- useless if since there is already global exception handler
        ApiResponse<Car> response =
                new ApiResponse<>(
                        true,
                        "Vehicle retrieved successfully",
                        car
                );
        return ResponseEntity.ok(response);
    }

    // post to /cars or add
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addCar(@Valid@RequestBody CreateCarRequest car){
        service.addCar(car);
        ApiResponse<Void> response =
                new ApiResponse<>(
                        true,
                        "Vehicle Added Successfully!",
                        null
                );

        return ResponseEntity.ok(response);
    }

    // deletes car based on ticket in /cars/{ticket}
    @DeleteMapping("/{ticket}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable int ticket){
        // boolean deleted = service.deleteCar(ticket);
        // if (deleted){
        //     return ResponseEntity.ok("Vehicle Deleted Successfully!");
        // }
        // return ResponseEntity.notFound().build(); -- now useless due to throw exception
        service.deleteCar(ticket);
         ApiResponse<Void> response =
                new ApiResponse<>(
                        true,
                        "Vehicle Deleted Successfully!",
                        null
                );

        return ResponseEntity.ok(response);
    }

    // update using put
    @PutMapping("/{ticket}")// can use PatchMapping("/{ticket}") for semantically correct
    public ResponseEntity<ApiResponse<Void>> updateCar(
        @PathVariable int ticket,
        @Valid@RequestBody UpdateCarRequest updatedCar
    ){
        // boolean updated = service.updateCar(ticket, updatedCar);

        // if(updated){
        //     return ResponseEntity.ok("Vehicle Updated!");
        // }
        // return ResponseEntity.notFound().build(); -- the throw exception is on effect
        service.updateCar(ticket, updatedCar);

        ApiResponse<Void> response =
                new ApiResponse<>(
                        true,
                        "Vehicle Updated Successfully!",
                        null
                );

        return ResponseEntity.ok(response);
    }
}
