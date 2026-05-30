package com.vehicle.vehicleapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.CarResponse;
import com.vehicle.vehicleapi.dto.ChangePasswordRequest;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.dto.UserResponse;
import com.vehicle.vehicleapi.mapper.CarMapper;
import com.vehicle.vehicleapi.mapper.UserMapper;
import com.vehicle.vehicleapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Users",
    description = """
            Operations Related to Users and its Operations
            """
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users/me")
public class UserController {
    private final UserService service;
    private final CarMapper carMapper;
    private final UserMapper userMapper;

    public UserController(UserService service, CarMapper carMapper, UserMapper userMapper){
        this.service = service;
        this.carMapper = carMapper;
        this.userMapper = userMapper;
    }

    // Endpoints for users and current users
    // endpoint for getting one user by id
    @Operation(
        summary = "Read Current User Data", 
        description = "Get current user information except for password"
    )
    @GetMapping
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
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteCurrentUser(){
        service.deleteCurrentUser();
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
    @PatchMapping
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
    @GetMapping("/cars")
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
    @PostMapping("/cars")
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
    @PatchMapping("/cars/{ticket}")
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
    @PatchMapping("/password")
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
