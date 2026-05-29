package com.vehicle.vehicleapi.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.CarResponse;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.dto.UserResponse;
import com.vehicle.vehicleapi.dto.UserSummaryResponse;
import com.vehicle.vehicleapi.mapper.CarMapper;
import com.vehicle.vehicleapi.mapper.UserMapper;
import com.vehicle.vehicleapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Users",
    description = """
            Operations Related to users and its cars
            """
)
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
    @Operation(summary = "Creates New User", description = "Adding/Registering more users")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addUser(
        @Parameter(
            description = "User Information Body"
        )
        @Valid@RequestBody CreateUserRequest user){
        URI location = URI.create("/users/"+user.getUsername());
        service.createUser(user);
        ApiResponse<Void> response = 
                new ApiResponse<>(
                    true,
                    "User Created!",
                    null
                );
        return ResponseEntity.created(location).body(response);
    }

    // Endpoint for getting all users
    @Operation(
        summary = "Read All Users Summary Data", 
        description = "Get all the users information except for password"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserSummaryResponse>>> getAllUsers(
        @Parameter(
            description = "Pageable Body"
        )
        Pageable pageable){
        Page<UserSummaryResponse> responses = service.getAllUser(pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All Users Retrieved!",
                        responses
            )
        );
    }

    // endpoint for getting one user by id
    @Operation(
        summary = "Read One User Data by ID", 
        description = "Get One user information except for password via User ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUsersById(
        @Parameter(
            description = "User ID"
        )
        @PathVariable Long id
    ){
        UserResponse response = userMapper.toResponse(
            service.getUserById(id)
        );
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "User found with ID "+response.getId(),
                response
            )
        );
    }

    // delete user and its owned cars
    @Operation(
        summary = "Deletes One User Data", 
        description = "Delete all the users information and Connected or Related Cars"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUsers(
        @Parameter(
            description = "User ID"
        )
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
    @Operation(
        summary = "Updates One User Data", 
        description = "Update or Edit One/All the users information except for password"
    )
    @PatchMapping("/{id}") // can also Use PutMapping but use here Patch due to semantics
    public ResponseEntity<ApiResponse<Void>> updateUsers(
        @Parameter(
            description = "User ID"
        )
        @PathVariable Long id,
        @Parameter(
            description = "User Information Body"
        )
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
    @Operation(
        summary = "Read One User Connected Cars", 
        description = "Get all the Cars or Vehicles Connected to User"
    )
    @GetMapping("/{id}/cars")
    public  ResponseEntity<ApiResponse<Page<CarResponse>>> getUserCars(
        @Parameter(
            description = "User ID"
        )
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
    @Operation(
        summary = "Create Car for User", 
        description = "Adding or Registering Cars to User"
    )
    @PostMapping("/{id}/cars")
    public ResponseEntity<ApiResponse<Void>> addUserCars(
        @Parameter(
            description = "User ID"
        )
        @PathVariable Long id,
        @Parameter(
            description = "Car Body Information"
        )
        @Valid @RequestBody CreateCarRequest request
    ){
        URI location = URI.create("/users/" +id+ "/cars");
        service.addUserCar(id, request);
        ApiResponse<Void> response = 
                new ApiResponse<>(
                    true,
                    "User Vehicle added successfully!",
                    null
                );
        return ResponseEntity.created(location).body(response);
    }

    // updating endpoint for car in user
    @Operation(
        summary = "Updates One Car on User", 
        description = "Update or Edit One/All the cars information"
    )
    @PatchMapping("/{id}/cars/{ticket}") // can also Use PutMapping but use here Patch due to semantics
    public ResponseEntity<ApiResponse<Void>> updateUserCars(
        @Parameter(
            description = "User ID"
        )
        @PathVariable Long id,
        @Parameter(
            description = "Vehicle ID"
        )
        @PathVariable Long ticket,
        @Parameter(
            description = "Car Information Body"
        )
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
