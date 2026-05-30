package com.vehicle.vehicleapi.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.LoginRequest;
import com.vehicle.vehicleapi.exception.RoleOutOfChoiceException;
import com.vehicle.vehicleapi.exception.UserAlreadyExistException;
import com.vehicle.vehicleapi.model.Role;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
        UserRepository repository,
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        PasswordEncoder passwordEncoder
    ){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    
    // adding users
    public void createUser(CreateUserRequest request){
        String email = request.getEmail().toLowerCase();
        String username = request.getUsername();

        if(repository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistException(
                "Email already taken!"
            );
        }

        if(repository.findByUsername(username).isPresent()){
            throw new UserAlreadyExistException(
                "Username already taken!"
            );
        }
        Set<Role> allowedRoles =
            Set.of(
                Role.USER,
                Role.MECHANIC,
                Role.GUIDE
            );

        if(!allowedRoles.contains(
            request.getRole()
        )){
            throw new RoleOutOfChoiceException(
                "Invalid role"
            );
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(
            passwordEncoder.encode(
                request.getPassword()
            )
        );
        user.setUsername(username);
        user.setRole(request.getRole());

        repository.save(user);
    }

    // log in user
    public String login(LoginRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword())  
        );
        return jwtService.generateToken(
            request.getUsername()
        );
    }

    public String test(String token){
        return jwtService.extractUsername(token)+" "+jwtService.extractExpiration(token);
    }
}
