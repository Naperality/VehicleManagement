package com.vehicle.vehicleapi.repository;

import com.vehicle.vehicleapi.model.Car;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.ArrayList;
// import java.util.List;


@Repository
public interface CarRepository extends JpaRepository<Car, Integer>{
    // private final List<Car> cars = new ArrayList<>();

    // public List<Car> findAll(){
    //     return cars;
    // }

    // public void saveCar(Car car){
    //     cars.add(car);
    // }

    // public boolean deleteByTicket(int ticket){
    //     return cars.removeIf(car -> car.getTicket()==ticket);
    // }

}
