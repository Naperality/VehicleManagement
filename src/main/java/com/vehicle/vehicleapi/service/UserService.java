package com.vehicle.vehicleapi.service;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.dto.ChangePasswordRequest;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.exception.UserNotFoundException;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.UserRepository;

@Service
public class UserService {
    // injection to service
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    public UserService(
        UserRepository repository, 
        PasswordEncoder passwordEncoder,
        AdminService adminService
    ){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }

    // get current user
    public User getCurrentUser(){
        Authentication authentication = 
            SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = authentication.getName();

        return repository
            .findByUsername(username)
            .orElseThrow(
                () -> new UserNotFoundException(
                    "User not Found"
                )
            );
    }

    // delete current user service
    public void deleteCurrentUser(){
        User user = getCurrentUser();
        adminService.deleteUser(user.getId());
    }

    // update details of current user
    public void updateCurrentUser(UpdateUserRequest request){
        User user = getCurrentUser();
        adminService.updateUser(user.getId(), request);
    }

    // read all cars owned by current user
    public Page<Car> getCurrentUserCar(Pageable pageable){
        User user = getCurrentUser();
        Page<Car> userCars = adminService.getUserCar(user.getId(), pageable);
        // Page<Car> userCars = carRepository.findByUserId(user.getId(), pageable);
        // if(userCars.isEmpty()){
        //     throw new CarNotFoundException(
        //         "User does not have cars yet!"
        //     );
        // }
        return userCars;
    }

    // put new car of current user
    public void addCurrentUserCar(CreateCarRequest request){
        User user = getCurrentUser();
        adminService.addUserCar(user.getId(), request);
    }

    // update old car of current user
    public void updateCurrentUserCar(Long ticket, UpdateCarRequest request){
        User user = getCurrentUser();
        adminService.updateUserCar(user.getId(), ticket, request);
    }

    // update password for current logged in user
    public void updateCurrentUserPassword(ChangePasswordRequest request){
        User user = getCurrentUser();
        if(!passwordEncoder.matches(
            request.getOldPassword(), 
            user.getPassword())){
                throw new IllegalArgumentException(
                    "Current Password is Incorrect!"
                );
            }
        
        if(passwordEncoder.matches(
            request.getNewPassword(), 
            user.getPassword())){
                throw new IllegalArgumentException(
                    "New Password is the same to old password!"
                );
            }
        user.setPassword(
            passwordEncoder.encode(
                request.getNewPassword()
            )
        );

        repository.save(user);
    }
}
