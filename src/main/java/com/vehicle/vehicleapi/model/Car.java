package com.vehicle.vehicleapi.model;

import jakarta.persistence.*;
import lombok.*; // this is the shortcut for getters and setters and etc.

// Create Entity to Table cars
@Entity
@Table(name = "cars")// created table cars
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    // Automatic Create ID's
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticket;

    @Column(name = "license_plate")// explicit column instead of hibernate auto
    private String licensePlate;
    @Column(name = "brand")
    private String brand;
    @Column(name = "model")
    private String model;
    @Column(name = "color")
    private String color;
    @Column(name = "fuel_type")
    private String fuelType;

    // all of this gone due to Lombok automatic
    // public Car() {} // for JPA, Hibernate internally creates objects dynamically.
    // public Car(
    //     Long ticket,
    //     String licensePlate,
    //     String brand,
    //     String model,
    //     String color,
    //     String fuelType
    // ){
    //     this.ticket = ticket;
    //     this.licensePlate = licensePlate;
    //     this.brand = brand;
    //     this.model = model;
    //     this.color = color;
    //     this.fuelType = fuelType;
    // }

    // // getters
    // public Long getTicket(){
    //     return ticket;
    // }
    // public String getLicensePlate(){
    //     return licensePlate;
    // }
    // public String getModel(){
    //     return model;
    // }
    // public String getBrand(){
    //     return brand;
    // }
    // public String getColor(){
    //     return color;
    // }
    // public String getFuelType(){
    //     return fuelType;
    // }

    // // setters
    // // public void setTicket(Long ticket){
    // //     this.ticket = ticket;
    // // } -- this is now useless cause it is an id and should not be changed
    // public void setLicensePlate(String licensePlate){
    //     this.licensePlate = licensePlate;
    // }
    // public void setModel(String model){
    //     this.model = model;
    // }
    // public void setBrand(String brand){
    //     this.brand = brand;
    // }
    // public void setColor(String color){
    //     this.color = color;
    // }
    // public void setFuelType(String fuelType){
    //     this.fuelType = fuelType;
    // }
}
