package com.vehicle.vehicleapi.service;

import com.vehicle.vehicleapi.dto.*;
import com.vehicle.vehicleapi.exception.*;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.model.Role;
import com.vehicle.vehicleapi.repository.UserRepository;
import com.vehicle.vehicleapi.repository.CarRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private UserService service;

    // Create User Success Test Case 
    @Test
    void shouldCreateUserSuccessfully() {

        CreateUserRequest request =
                new CreateUserRequest();

        request.setUsername("napoleon");
        request.setEmail("napoleon@email.com");
        request.setPassword("123456");
        request.setRole(Role.USER);

        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        service.createUser(request);

        verify(repository, times(1))
                .save(any(User.class));
    }

    // duplicate email testing
    @Test
    void shouldThrowExceptionWhenEmailExists() {

        CreateUserRequest request =
                new CreateUserRequest();

        request.setEmail("test@email.com");
        request.setUsername("napoleon");

        User existing = new User();

        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.of(existing));

        assertThrows(
                UserAlreadyExistException.class,
                () -> service.createUser(request)
        );

        verify(repository, never())
                .save(any());
    }

    // duplicate username testing
    @Test
    void shouldThrowExceptionWhenUsernameExists() {

        CreateUserRequest request =
                new CreateUserRequest();

        request.setEmail("new@email.com");
        request.setUsername("napoleon");

        when(repository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        when(repository.findByUsername(anyString()))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                UserAlreadyExistException.class,
                () -> service.createUser(request)
        );

        verify(repository, never())
                .save(any());
    }

    //get user by id success test case
    @Test
    void shouldReturnUserById() {

        User user = new User();

        user.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = service.getUserById(1L);

        assertEquals(
                1L,
                result.getId()
        );
    }

    // Id not found test case
    @Test
    void shouldThrowWhenUserNotFound() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.getUserById(1L)
        );
    }

    // delete user test case
    @Test
    void shouldDeleteUser() {

        User user = new User();

        user.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        service.deleteUser(1L);

        verify(repository)
                .delete(user);
    }

    // add user car test case
    @Test
    void shouldAddCarToUser() {

        User user = new User();
        user.setId(1L);

        CreateCarRequest request =
                new CreateCarRequest();

        request.setLicensePlate("ABC123");

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(carRepository.findByLicensePlate("ABC123"))
                .thenReturn(Optional.empty());

        service.addUserCar(1L, request);

        verify(carRepository)
                .save(any(Car.class));
    }

    // duplicate plate or license plate
    @Test
    void shouldThrowWhenPlateAlreadyExists() {

        User user = new User();

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        CreateCarRequest request =
                new CreateCarRequest();

        request.setLicensePlate("ABC123");

        when(carRepository.findByLicensePlate("ABC123"))
                .thenReturn(Optional.of(new Car()));

        assertThrows(
                CarAlreadyExistException.class,
                () -> service.addUserCar(1L, request)
        );
    }

    // update user test case
    @Test
    void shouldUpdateUserInformation() {

        User user = new User();

        user.setId(1L);
        user.setUsername("old");

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setUsername("new");

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(repository.existsByUsernameAndIdNot(
                "new",
                1L
        )).thenReturn(false);

        service.updateUser(1L, request);

        assertEquals(
                "new",
                user.getUsername()
        );

        verify(repository)
                .save(user);
    }

    // pageable test cases getting all users
    @Test
    void shouldReturnAllUsers() {

        Pageable pageable = PageRequest.of(0, 10);

        UserSummaryResponse response =
            new UserSummaryResponse(
                1L,
                "napoleon",
                "test@email.com",
                Role.USER,
                2
            );

        Page<UserSummaryResponse> page =
            new PageImpl<>(
                List.of(response)
            );

        when(repository.findAllUserResponses(pageable))
            .thenReturn(page);

        Page<UserSummaryResponse> result =
            service.getAllUser(pageable);

        assertEquals(1, result.getTotalElements());
    }

    // getting all user empty test case
    @Test
    void shouldThrowWhenNoUsersExist() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<UserSummaryResponse> page =
            Page.empty();

        when(repository.findAllUserResponses(pageable))
            .thenReturn(page);

        assertThrows(
            UserNotFoundException.class,
            () -> service.getAllUser(pageable)
        );
    }

    // getting user car success test case
    @Test
    void shouldReturnUserCars() {

        User user = new User();

        user.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Car car = new Car();

        car.setTicket(1L);

        Page<Car> cars =
            new PageImpl<>(
                List.of(car)
            );

        when(repository.findById(1L))
            .thenReturn(Optional.of(user));

        when(carRepository.findByUserId(
            1L,
            pageable
        )).thenReturn(cars);

        Page<Car> result =
            service.getUserCar(
                1L,
                pageable
            );

        assertEquals(
            1,
            result.getTotalElements()
        );
    }

    // get user car empty test case
    @Test
    void shouldThrowWhenUserHasNoCars() {

        User user = new User();

        user.setId(1L);

        Pageable pageable =
            PageRequest.of(0,10);

        when(repository.findById(1L))
            .thenReturn(Optional.of(user));

        when(carRepository.findByUserId(
            1L,
            pageable
        )).thenReturn(Page.empty());

        assertThrows(
            CarNotFoundException.class,
            () -> service.getUserCar(
                1L,
                pageable
            )
        );
    }

    // update user car success
    @Test
    void shouldUpdateUserCar() {

        Car car = new Car();

        car.setTicket(1L);
        car.setBrand("Toyota");

        UpdateCarRequest request =
            new UpdateCarRequest();

        request.setBrand("Honda");

        when(
            carRepository.findByTicketAndUserId(
                1L,
                1L
            )
        ).thenReturn(Optional.of(car));

        service.updateUserCar(
            1L,
            1L,
            request
        );

        assertEquals(
            "Honda",
            car.getBrand()
        );

        verify(carRepository)
            .save(car);
    }

    // update user car not found testing
    @Test
    void shouldThrowWhenCarNotFound() {

        UpdateCarRequest request =
            new UpdateCarRequest();

        when(
            carRepository.findByTicketAndUserId(
                1L,
                1L
            )
        ).thenReturn(Optional.empty());

        assertThrows(
            CarNotFoundException.class,
            () -> service.updateUserCar(
                1L,
                1L,
                request
            )
        );
    }

    // update user car duplicate test case
    @Test
    void shouldThrowWhenPlateAlreadyExistsUpdateCar() {

        UpdateCarRequest request =
            new UpdateCarRequest();

        request.setLicensePlate("ABC123");

        assertThrows(
            CarNotFoundException.class,
            () -> service.updateUserCar(
                1L,
                1L,
                request
            )
        );
    }

    // delete user not found
    @Test
    void shouldThrowWhenDeletingMissingUser() {

        when(repository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(
            UserNotFoundException.class,
            () -> service.deleteUser(1L)
        );
    }

    // duplicate email user test case

    @Test
    void shouldThrowWhenUpdatingToExistingEmail() {

        User user = new User();
        user.setId(1L);

        UpdateUserRequest request =
            new UpdateUserRequest();

        request.setEmail("taken@email.com");

        when(repository.findById(1L))
            .thenReturn(Optional.of(user));

        when(
            repository.existsByEmailAndIdNot(
                "taken@email.com",
                1L
            )
        ).thenReturn(true);

        assertThrows(
            UserAlreadyExistException.class,
            () -> service.updateUser(
                1L,
                request
            )
        );
    }

    // duplicate username update test case
    @Test
    void shouldThrowWhenUpdatingToExistingUsername() {

        User user = new User();
        user.setId(1L);

        UpdateUserRequest request =
            new UpdateUserRequest();

        request.setUsername("existing");

        when(repository.findById(1L))
            .thenReturn(Optional.of(user));

        when(
            repository.existsByUsernameAndIdNot(
                "existing",
                1L
            )
        ).thenReturn(true);

        assertThrows(
            UserAlreadyExistException.class,
            () -> service.updateUser(
                1L,
                request
            )
        );
    }

    // missing user to adding car test case
    @Test
    void shouldThrowWhenAddingCarToMissingUser() {

        when(repository.findById(1L))
            .thenReturn(Optional.empty());

        CreateCarRequest request =
            new CreateCarRequest();

        assertThrows(
            UserNotFoundException.class,
            () -> service.addUserCar(
                1L,
                request
            )
        );
    }

    // getting cars of missing users test case
    @Test
    void shouldThrowWhenGettingCarsOfMissingUser() {

        Pageable pageable =
            PageRequest.of(0, 10);

        when(repository.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(
            UserNotFoundException.class,
            () -> service.getUserCar(
                1L,
                pageable
            )
        );
    }

}