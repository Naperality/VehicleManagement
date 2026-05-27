package com.vehicle.vehicleapi.controller;

import com.vehicle.vehicleapi.service.CarService;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.SearchCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.CarResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

// import java.util.List;

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
    public ResponseEntity<ApiResponse<Page<Car>>> getAllCars(
        Pageable pageable
    ){
        ApiResponse<Page<Car>> response =
                new ApiResponse<>(
                        true,
                        "Vehicles retrieved successfully",
                        service.getAllCars(pageable)
                );

        return ResponseEntity.ok(response);
    }

    //get specified car in /cars/{ticket}
    @GetMapping("/{ticket}")
    public ResponseEntity<ApiResponse<Car>> getCar(@PathVariable Long ticket){
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

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // deletes car based on ticket in /cars/{ticket}
    @DeleteMapping("/{ticket}")
    public ResponseEntity<ApiResponse<Void>> deleteCar(@PathVariable Long ticket){
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
        @PathVariable Long ticket,
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

    // add new end point for new created methods
    @GetMapping("/plate/{plate}")
    public ResponseEntity<ApiResponse<Car>> getByPlate(
        @PathVariable String plate
    ){
        Car car = service.getByLicensePlate(plate);
  
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Vehicles Found!",
                        car
                )
        );
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<ApiResponse<Page<Car>>>  getByBrand(
        @PathVariable String brand, Pageable pageable
    ){
        Page<Car> cars = service.getByBrand(brand, pageable);
        ApiResponse<Page<Car>> response = 
                new ApiResponse<>(
                        true,
                        "Vehicles Found!",
                        cars
                );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<ApiResponse<Page<Car>>> getByModel(
        @PathVariable String model, Pageable pageable
    ){
        Page<Car> cars = service.getByModel(model, pageable);
        ApiResponse<Page<Car>> response = 
                new ApiResponse<>(
                        true,
                        "Vehicles Found!",
                        cars
                );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fuel/{fuel}")
    public ResponseEntity<ApiResponse<Page<Car>>> getByFuel(
        @PathVariable String fuel,Pageable pageable
    ){
        Page<Car> cars = service.getByFuelType(fuel, pageable);
        ApiResponse<Page<Car>> response = 
                new ApiResponse<>(
                        true,
                        "Vehicles Found!",
                        cars
                );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<ApiResponse<Page<Car>>> getByColor(
        @PathVariable String color, Pageable pageable
    ){
        Page<Car> cars = service.getByColor(color, pageable);
        ApiResponse<Page<Car>> response = 
                new ApiResponse<>(
                        true,
                        "Vehicles Found!",
                        cars
                );
        
        return ResponseEntity.ok(response);
    }

    // Endpoint for searching
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<Car>>> searchCars(
        SearchCarRequest request,
        Pageable pageable
    ){
        Page<Car> cars = service.searchCars(request, pageable);
        ApiResponse<Page<Car>> response = 
                        new ApiResponse<>(
                                true,
                                "Vehicles Found!",
                                cars
                        );
        return ResponseEntity.ok(response);
    }

    // Endpoint for user and his/her cars
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<Car>>> getCarsByUser(
        @PathVariable Long userId, Pageable pageable
    ){
        Page<Car> cars = service.getCarsByUser(userId, pageable);
        ApiResponse<Page<Car>> response = 
                        new ApiResponse<>(
                                true,
                                "Users Vehicles Found!",
                                cars
                        );
        return ResponseEntity.ok(response);
    }
}
