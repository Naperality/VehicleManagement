package com.vehicle.vehicleapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.CarResponse;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.dto.UserResponse;
import com.vehicle.vehicleapi.mapper.CarMapper;
import com.vehicle.vehicleapi.mapper.UserMapper;
import com.vehicle.vehicleapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final CarMapper carMapper;
    private final UserMapper userMapper;

    public UserController(UserService service, CarMapper carMapper, UserMapper userMapper){
        this.service = service;
        this.carMapper = carMapper;
        this.userMapper = userMapper;
    }

    // Endpoint for adding users
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addUser(@Valid@RequestBody CreateUserRequest user){
        service.createUser(user);
        ApiResponse<Void> response = 
                new ApiResponse<>(
                    true,
                    "User Created!",
                    null
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint for getting all users
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(Pageable pageable){
        Page<UserResponse> responses = service.getAllUser(pageable).map(userMapper::toResponse);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All Users Retrieved!",
                        responses
            )
        );
    }

    // delete user and its owned cars
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUsers(
        @PathVariable Long id
    ){
        service.deleteUser(id);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "User and User Cars Deleted",
                null
            )
        );
    }

    // endpoint for updating user info
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUsers(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ){
        service.updateUser(id, request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "User Information Updated Successfully!",
                null
            )
        );
    }

    // endpoint for user owned cars
    @GetMapping("/{id}/cars")
    public  ResponseEntity<ApiResponse<Page<CarResponse>>> getUserCars(
        @PathVariable Long id, Pageable pageable
    ){
        Page<Car> userCars = service.getUserCar(id, pageable);
        Page<CarResponse> responses = userCars.map(carMapper::toResponse);
        return ResponseEntity.ok(
            new ApiResponse<>(
                    true,
                    "User cars are found!",
                    responses
            )
        );
    }

    // setting cars to user
    @PostMapping("/{id}/cars")
    public ResponseEntity<ApiResponse<Void>> addUserCars(
        @PathVariable Long id,
        @Valid @RequestBody CreateCarRequest request
    ){
        service.addUserCar(id, request);
        ApiResponse<Void> response = 
                new ApiResponse<>(
                    true,
                    "User Vehicle added successfully!",
                    null
                );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // updating endpoint for car in user
    @PutMapping("/{id}/cars/{ticket}")
    public ResponseEntity<ApiResponse<Void>> updateUserCars(
        @PathVariable Long id,
        @PathVariable Long ticket,
        @Valid @RequestBody UpdateCarRequest request
    ){
        service.updateUserCar(id, ticket, request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Vehicle Updated!",
                null
            )
        );
    }
}
