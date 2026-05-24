package com.vehicle.vehicleapi.repository;

import com.vehicle.vehicleapi.model.Car;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class CarRepository {
    private final List<Car> cars = new ArrayList<>();

    public List<Car> findAll(){
        return cars;
    }

    public void saveCar(Car car){
        cars.add(car);
    }

    public boolean deleteByTicket(int ticket){
        return cars.removeIf(car -> car.getTicket()==ticket);
    }

}
