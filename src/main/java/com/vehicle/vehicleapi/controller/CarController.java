package com.vehicle.vehicleapi.controller;

import com.vehicle.vehicleapi.service.CarService;
import com.vehicle.vehicleapi.model.Car;

import org.springframework.web.bind.annotation.*;

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
    public List<Car> getAllCars(){
        return service.getAllCars();
    }

    //get specified car in /cars/{ticket}
    @GetMapping("/{ticket}")
    public Car getCar(@PathVariable int ticket){
        return service.getCarByTicket(ticket);
    }

    // post to /cars or add
    @PostMapping
    public void addCar(@RequestBody Car car){
        service.addCar(car);
    }

    // deletes car based on ticket in /cars/{ticket}
    @DeleteMapping("/{ticket}")
    public String deleteCar(@PathVariable int ticket){
        boolean deleted = service.deleteCar(ticket);
        if (deleted){
            return "Vehicle Deleted Successfullt!";
        }
        return "Vehicle Not Found!";
    }

    // update using put
    @PutMapping("/{ticket}")
    public String updateCar(
        @PathVariable int ticket,
        @RequestBody Car updatedCar
    ){
        boolean updated = service.updateCar(ticket, updatedCar);

        if(updated){
            return "Vehicle Updated Successfully!";
        }
        return "Vehicle Not Found!";
    }
}
