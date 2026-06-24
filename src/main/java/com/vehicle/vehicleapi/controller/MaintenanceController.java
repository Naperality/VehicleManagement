package com.vehicle.vehicleapi.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.CreateMaintenanceRequest;
import com.vehicle.vehicleapi.dto.MaintenanceResponse;
import com.vehicle.vehicleapi.dto.UpdateMaintenanceRequest;
import com.vehicle.vehicleapi.service.MaintenanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users/me/cars")
@SecurityRequirement(name = "bearerAuth")
@Tag(
    name = "Maintenance Records",
    description = "Operations related to vehicle maintenance records"
)
public class MaintenanceController {
    private final MaintenanceService service;

    public MaintenanceController(
        MaintenanceService service
    ){
        this.service = service;
    }

    // endpoint for creating records
    @Operation(
        summary = "Creates Record",
        description = "Adds New Records of different types" 
    )
    @PostMapping("/{ticket}/maintenance")
    public ResponseEntity<ApiResponse<Void>> createRecord(
        @Parameter(
            description = "Ticket for vehicle"
        )
        @PathVariable Long ticket,

        @Parameter(
            description = "Information of the record"
        )

        @Valid  @RequestBody CreateMaintenanceRequest request
    ){
        service.createRecord(ticket, request);
        URI location = URI.create("/users/me/cars/"+ticket+"/maintenance");
        return ResponseEntity.created(location).body(
            new ApiResponse<>(
                true,
                "Created New Record!",
                null
            )
        );
    }

    // endpoint for get current user records
    @Operation(
        summary = "Current User Records",
        description = "Gets all the current user records"
    )







    
    @GetMapping("/maintenance")
    public ResponseEntity<ApiResponse<Page<MaintenanceResponse>>> getUserRecords(
        @Parameter(
            description = "Pageable Information"
        )
        Pageable pageable
    ){
        Page<MaintenanceResponse> records = service.getUserRecords(pageable);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Records Retireved Successfully!",
                records
            )
        );
    }

    // endpoint for get current user current car records
    @Operation(
        summary = "Current Car Record",
        description = "Gets current car records of currently logged in user"
    )
    @GetMapping("/{ticket}/maintenance")
    public ResponseEntity<ApiResponse<Page<MaintenanceResponse>>> getCurrentCarRecords(
        @Parameter(
            description = "Ticket ID for Car"
        )
        @PathVariable Long ticket,
        @Parameter(
            description = "Pageable information"
        )
        Pageable pageable
    ){
        Page<MaintenanceResponse> records = service.getCarRecords(ticket, pageable);
        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Retrieved of Current Car Record Successfully!",
                records
            )
        );
    }

    // delete record endpoint
    @DeleteMapping("/{ticket}/maintenance/{recordId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(
        @PathVariable Long ticket,
        @PathVariable Long recordId
    ){
        service.deleteRecord(ticket, recordId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Record Deleted Successfully!",
                null
            )
        );
    }

    // update endpoint
    @PatchMapping("/{ticket}/maintenance/{recordId}")
    public ResponseEntity<ApiResponse<Void>> updateRecord(
        @PathVariable Long ticket,
        @PathVariable Long recordId,

        @Valid
        @RequestBody
        UpdateMaintenanceRequest request
    ){
        service.updateRecord(
            ticket,
            recordId,
            request
        );

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Record Updated Successfully!",
                null
            )
        );
    }
}
