package com.vehicle.vehicleapi.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicle.vehicleapi.dto.ApiResponse;
import com.vehicle.vehicleapi.dto.AuthResponse;
import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.LoginRequest;
import com.vehicle.vehicleapi.service.AuthService;
import com.vehicle.vehicleapi.service.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authorization",
    description = """
            Operations Related to Authorization and Public Log in/Register
            """
)
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService service;
    private final JwtService jwtService;

    public AuthController(AuthService service, JwtService jwtService){
        this.service = service;
        this.jwtService = jwtService;
    }
    // Endpoint for adding users
    // @PreAuthorize("hasRole('ADMIN')") -- allow to public for registrations
    @Operation(summary = "Creates New User", description = "Adding/Registering more users")
    @PostMapping("/register")
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

    // Login success for users before authentication JWT
    @Operation(
        summary = "Log in Request",
        description = "Try to log in and verify"
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(
            service.login(request)
        );
    }

    @Operation(summary = "Temporary Testing Tokens")
    @GetMapping("/test")
    public ResponseEntity<?> testJwt(
        @RequestHeader("Authorization")
        String authHeader
    ){
        String token = authHeader.substring(7);

        Map<String,Object> data = Map.of(
            "username", jwtService.extractUsername(token),
            "role", jwtService.extractRole(token),
            "userId", jwtService.extractUserId(token),
            "email", jwtService.extractEmail(token)
        );

        return ResponseEntity.ok(data);
    }
}
