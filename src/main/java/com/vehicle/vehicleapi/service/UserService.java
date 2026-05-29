package com.vehicle.vehicleapi.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.dto.UserSummaryResponse;
import com.vehicle.vehicleapi.exception.CarAlreadyExistException;
import com.vehicle.vehicleapi.exception.CarNotFoundException;
import com.vehicle.vehicleapi.exception.UserAlreadyExistException;
import com.vehicle.vehicleapi.exception.UserNotFoundException;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.repository.CarRepository;
import com.vehicle.vehicleapi.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;
    private final CarRepository carRepository;

    public UserService(UserRepository repository, CarRepository carRepository){
        this.repository = repository;
        this.carRepository = carRepository;
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

        User user = new User();
        user.setEmail(email);
        user.setPassword(request.getPassword());
        user.setUsername(username);
        user.setRole(request.getRole());

        repository.save(user);
    }

    // getting all users
    public Page<UserSummaryResponse> getAllUser(Pageable pageable){
        Page<UserSummaryResponse> users = repository.findAllUserResponses(pageable);
        if(users.isEmpty()){
            throw new UserNotFoundException(
                "No Users Found!"
            );
        }
        return users;
    }

    // get user by id
    public User getUserById(Long id){
        return repository.findById(id).orElseThrow(
            () -> new UserNotFoundException(
                "No User found with ID: "+id
            )
        );
    }

    // update user info
    public void updateUser(Long id, UpdateUserRequest request){
        User user = getUserById(id);

        if(request.getEmail() != null &&
        repository.existsByEmailAndIdNot(
            request.getEmail().toLowerCase(),
            id
        )){

            throw new UserAlreadyExistException(
                "Email already taken!"
            );
        }
        if(request.getUsername() != null &&
        repository.existsByUsernameAndIdNot(
            request.getUsername(),
            id
        )){

            throw new UserAlreadyExistException(
                "Username already taken!"
            );
        }
        // boolean alreadyExist = repository.findByEmail(request.getEmail().toLowerCase()).isPresent() || 
        //                        repository.findByUsername(request.getUsername()).isPresent();
        // if(alreadyExist){
        //     throw new UserAlreadyExistException(
        //         "User email or username is already taken!"
        //     );
        // } -- risky due to nullable values

        if(request.getUsername() != null){
            user.setUsername(request.getUsername());
        }
        if(request.getEmail() != null){
            user.setEmail(request.getEmail().toLowerCase());
        }
        if(request.getRole() != null){
            user.setRole(request.getRole());
        }
        repository.save(user);
    }

    // deleting user and its corresponding cars connected
    public void deleteUser(Long id){
        User user = getUserById(id);
        repository.delete(user);
    }

    // getting user owned cars
    public Page<Car> getUserCar(Long id, Pageable pageable){
        getUserById(id);
        Page<Car> userCars = carRepository.findByUserId(id, pageable);
        if(userCars.isEmpty()){
            throw new CarNotFoundException(
                "User does not have cars yet!"
            );
        }
        return userCars;
    }

    // adding cars to users service
    public void addUserCar(Long id, CreateCarRequest request){
        User user = getUserById(id);

        // checking if no duplicates of license plates
        if(carRepository.findByLicensePlate(request.getLicensePlate()).isPresent()){
            throw new CarAlreadyExistException(
                "Car Plate Number Already Exist with plate "+request.getLicensePlate()
            );
        }
        Car car = new Car(); 

        car.setLicensePlate(request.getLicensePlate());
        car.setBrand(request.getBrand());
        car.setColor(request.getColor());
        car.setFuelType(request.getFuelType());
        car.setModel(request.getModel());
        car.setUser(user);

        carRepository.save(car);
    }

    // update user car 
    public void updateUserCar(Long id, Long ticket, UpdateCarRequest request){
        Car car = carRepository.
            findByTicketAndUserId(ticket, id).
                orElseThrow(
                    () -> new CarNotFoundException(
                        "Vehicle not found on this user!"
                    )
                );
        if(request.getLicensePlate() != null &&
            carRepository.existsByLicensePlateAndTicketNot(
                request.getLicensePlate(),
                ticket
            )){

            throw new CarAlreadyExistException(
                "License plate already exists!"
            );
        }
        if(request.getBrand() != null){
            car.setBrand(request.getBrand());
        }
        if(request.getColor() != null){
            car.setColor(request.getColor());
        }
        if(request.getFuelType() != null){
            car.setFuelType(request.getFuelType());
        }
        if(request.getLicensePlate() != null){
            car.setLicensePlate(request.getLicensePlate());
        }
        if(request.getModel() != null){
            car.setModel(request.getModel());
        }
        carRepository.save(car);
    }
}
