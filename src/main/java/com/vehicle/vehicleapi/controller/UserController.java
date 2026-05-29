package com.vehicle.vehicleapi.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.vehicle.vehicleapi.dto.ChangePasswordRequest;
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
    // @PreAuthorize("hasRole('ADMIN')") -- allow to public for registrations
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Read One User Connected Cars", 
        description = "Get all the Cars or Vehicles Connected to User"
    )
    @GetMapping("/{id}/cars")
    public  ResponseEntity<ApiResponse<Page<CarResponse>>> getUserCars(
        @Parameter(
            description = "User ID"
        )
        @PathVariable Long id, 
        @Parameter(
            description = "Pageable Body"
        )
        Pageable pageable
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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

    // Endpoints for users and current users
    // endpoint for getting one user by id
    @Operation(
        summary = "Read Current User Data", 
        description = "Get current user information except for password"
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(){
        UserResponse response = userMapper.toResponse(
            service.getCurrentUser()
        );
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Current User Retrieved",
                response
            )
        );
    }

    // endpoint for delete own account
    @Operation(
        summary = "Deletes Current Account",
        description = "Deletes current user account along its cars"
    )
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteCurrentUser(){
        service.deleteUser(service.getCurrentUser().getId());
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Current User Deleted!",
                null
            )
        );
    }

    // endpoint for update current user details
    @Operation(
        summary = "Update Current User Details",
        description = "Updates/Edit Current logged in user except password"
    )
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateCurrentUser(
        @Parameter(
            description = "Current User Updated Details"
        )
        @Valid@RequestBody UpdateUserRequest request
    ){
        service.updateCurrentUser(request); 
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Profile Updated Successfully!",
                null
            )
        );
    }

    // endpoint for getting all cars of current user
    @Operation(
        summary = "Read Current User Car",
        description = "Get all cars connected to current or logged in user"
    )
    @GetMapping("/me/cars")
    public ResponseEntity<ApiResponse<Page<CarResponse>>> getCurrentUserCar(
        @Parameter(
            description = "Pageable Body"
        )
        Pageable pageable
    ){
        Page<Car> cars = service.getCurrentUserCar(pageable);
        Page<CarResponse> responses = cars.map(carMapper::toResponse);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Cars retrieved",
                responses
            )
        );
    }

    // adding or creating new cars to current user
    @Operation(
        summary = "Create New Cars to Current User",
        description = "Add or Register More cars to current logged in user"
    )
    @PostMapping("/me/cars")
    public ResponseEntity<ApiResponse<Void>> addCurrentUserCar(
        @Parameter(
            description = "Car Body Information"
        )
        @Valid @RequestBody CreateCarRequest request
    ){
        service.addCurrentUserCar(request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Added Vehicle Successfully!",
                null
            )
        );
    }

    // update specific car of current user endpoint
    @Operation(
        summary = "Update Specific Car of Current User",
        description = "Update/Edit Specific Car of current or logged in user via Car Ticket"
    )
    @PatchMapping("/me/cars/{ticket}")
    public ResponseEntity<ApiResponse<Void>> updateCurrentUserCar(
        @Parameter(
            description = "Vehicle ID or Ticket"
        )
        @PathVariable Long ticket,
        @Parameter(
            description = "Vehicle Body Updates"
        )
        @Valid @RequestBody UpdateCarRequest request
    ){
        service.updateCurrentUserCar(ticket, request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Update Car Successfully!",
                null
            )
        );
    }

    // update password for current user endpoint
    @Operation(
        summary = "Update Password Current User",
        description = "Update/Edit Password for current logged in user"
    )
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updateCurrentUserPassword(
        @Parameter(
            description = "Current User new and old passwords"
        )
        @Valid @RequestBody ChangePasswordRequest request
    ){
        service.updateCurrentUserPassword(request);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Change Password Successfully!",
                null
            )
        );
    }
}
