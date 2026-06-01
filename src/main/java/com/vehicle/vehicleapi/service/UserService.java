package com.vehicle.vehicleapi.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.dto.ChangePasswordRequest;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.SearchCarRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.exception.CarNotFoundException;
import com.vehicle.vehicleapi.exception.UserNotFoundException;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.CarRepository;
import com.vehicle.vehicleapi.repository.UserRepository;
import com.vehicle.vehicleapi.specification.CarSpecification;

@Service
public class UserService {
    // injection to service
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final CarRepository carRepository;

    public UserService(
        UserRepository repository, 
        PasswordEncoder passwordEncoder,
        CarRepository carRepository,
        AdminService adminService
    ){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
        this.carRepository = carRepository;
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
    // public Page<Car> getCurrentUserCar(Pageable pageable){
    //     User user = getCurrentUser();
    //     Page<Car> userCars = adminService.getUserCar(user.getId(), pageable);
    //     // Page<Car> userCars = carRepository.findByUserId(user.getId(), pageable);
    //     // if(userCars.isEmpty()){
    //     //     throw new CarNotFoundException(
    //     //         "User does not have cars yet!"
    //     //     );
    //     // }
    //     return userCars;
    // }

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

    // delete current user car
    public void deleteCurrentUserCar(Long ticket){
        User user = getCurrentUser();
        Optional<Car> car = carRepository.findByTicketAndUserId(ticket, user.getId());
        if(car.isEmpty()){
            throw new CarNotFoundException(
                "Car not found!"
            );
        }
        carRepository.deleteById(ticket);
        
    }

    // searching cars
    public Page<Car> searchCurrentUserCars(SearchCarRequest request, Pageable pageable){
        User user = getCurrentUser();
        Specification<Car> spec =
        Specification.where(
            CarSpecification.belongsToUser(user.getId())
        );
        if(request.getBrand() != null){
            spec = spec.and(
                CarSpecification.brandContains(
                    request.getBrand()
                )
            );
        }

        if(request.getModel() != null){
            spec = spec.and(
                CarSpecification.modelContains(
                    request.getModel()
                )
            );
        }

        if(request.getColor() != null){
            spec = spec.and(
                CarSpecification.colorContains(
                    request.getColor()
                )
            );
        }

        if(request.getFuelType() != null){
            spec = spec.and(
                CarSpecification.fuelTypeContains(
                    request.getFuelType()
                )
            );
        }

        if(request.getLicensePlate() != null){
            spec = spec.and(
                CarSpecification.licensePlateContains(
                    request.getLicensePlate()
                )
            );
        }
        Page<Car> cars =
        carRepository.findAll(
            spec,
            pageable
        );
        return cars;
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
