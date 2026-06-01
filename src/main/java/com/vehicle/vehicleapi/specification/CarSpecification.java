package com.vehicle.vehicleapi.specification;

import org.springframework.data.jpa.domain.Specification;

import com.vehicle.vehicleapi.model.Car;

public class CarSpecification {

    public static Specification<Car> belongsToUser (Long userId){
        return (root, query, cb) ->
            cb.equal(
                root.get("user").get("id"),
                userId
            );
    }

     public static Specification<Car> brandContains(String brand){
        return (root, query, cb) ->
            cb.like(
                cb.lower(root.get("brand")),
                "%" + brand.toLowerCase() + "%"
            );
    }

    public static Specification<Car> modelContains(String model){
        return (root, query, cb) ->
            cb.like(
                cb.lower(root.get("model")),
                "%" + model.toLowerCase() + "%"
            );
    }

    public static Specification<Car> colorContains(String color){
        return (root, query, cb) ->
            cb.like(
                cb.lower(root.get("color")),
                "%" + color.toLowerCase() + "%"
            );
    }

    public static Specification<Car> fuelTypeContains(String fuelType){
        return (root, query, cb) ->
            cb.like(
                cb.lower(root.get("fuelType")),
                "%" + fuelType.toLowerCase() + "%"
            );
    }

    public static Specification<Car> licensePlateContains(String plate){
        return (root, query, cb) ->
            cb.like(
                cb.lower(root.get("licensePlate")),
                "%" + plate.toLowerCase() + "%"
            );
    }
    
}
