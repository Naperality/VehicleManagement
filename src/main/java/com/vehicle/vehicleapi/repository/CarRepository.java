package com.vehicle.vehicleapi.repository;

import com.vehicle.vehicleapi.model.Car;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;

// import java.util.ArrayList;
// import java.util.List;


@Repository
public interface CarRepository extends JpaRepository<Car, Long>{
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

    // creating own methods and used optional
    Optional<Car> findByLicensePlate(String licensePlate);
    Page<Car> findByBrand(String brand, Pageable pageable); // SELECT * FROM cars WHERE brand=?
    Page<Car> findByModel(String model, Pageable pageable);
    Page<Car> findByFuelType(String fuel, Pageable pageable);
    Page<Car> findByColor(String color, Pageable pageable);

    // query common conditions and search using Derived Query Methods
    // SELECT * FROM cars WHERE brand=? and color=?
    Page<Car> findByBrandAndColor(String brand, String Color, Pageable pageable); 
    // using Containing, StartingWith, and Endingwith
    Page<Car> findByBrandContaining(String brand, Pageable pageable); // WHERE brand LIKE '%Toy%'
    Page<Car> findByBrandEndingWith(String brand, Pageable pageable); // ends with the brand=?
    Page<Car> findByBrandStartingWith(String brand, Pageable pageable); // starts with the brand=?

    Page<Car> findByUserId(Long userId, Pageable pageable);
    Optional<Car> findByTicketAndUserId(Long ticket, Long userId);
    
    boolean existsByLicensePlateAndTicketNot(
        String licensePlate,
        Long ticket
    );
    // JPQL or Java Persistence Query Language, uses objects instead of tables
    @Query("""
            SELECT c
            FROM Car c
            WHERE
                (:licensePlate IS NULL OR c.licensePlate = :licensePlate)
            AND
                (:model IS NULL OR c.model = :model)
            AND 
                (:brand IS NULL OR c.brand = :brand)
            AND
                (:color IS NULL OR c.color = :color)
            AND
                (:fuelType IS NULL OR c.fuelType = :fuelType)
        """)
    Page<Car> searchCars(
        @Param("licensePlate") String licensePlate,
        @Param("brand") String brand,
        @Param("model") String model,
        @Param("color") String color,
        @Param("fuelType") String fuelType,
        Pageable pageable
    );

    // Native SQL version
    @Query(value = """
            SELECT * FROM cars
            WHERE (:licensePlate IS NULL OR license_plate = :licensePlate)
            AND (:brand IS NULL OR brand = :brand)
            AND (:model IS NULL OR model = :model)
            AND (:color IS NULL OR color = :color)
            AND (:fuelType IS NULL OR fuel_type = :fuelType)
            """,
        countQuery = """
                SELECT COUNT(*) FROM cars
                WHERE (:licensePlate IS NULL OR license_plate = :licensePlate)
                AND (:brand IS NULL OR brand = :brand)
                AND (:model IS NULL OR model = :model)
                AND (:color IS NULL OR color = :color)
                AND (:fuelType IS NULL OR fuel_type = :fuelType)
                """,
            nativeQuery = true)
    Page<Car> searchCarsNative(
        @Param("licensePlate") String licensePlate,
        @Param("brand") String brand,
        @Param("model") String model,
        @Param("color") String color,
        @Param("fuelType") String fuelType,
        Pageable pageable
    );
}
