package com.vehicle.vehicleapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

// import com.vehicle.vehicleapi.controller.UserController;
import com.vehicle.vehicleapi.dto.CarResponse;
import com.vehicle.vehicleapi.dto.CreateCarRequest;
import com.vehicle.vehicleapi.dto.CreateUserRequest;
import com.vehicle.vehicleapi.dto.UpdateCarRequest;
import com.vehicle.vehicleapi.dto.UpdateUserRequest;
import com.vehicle.vehicleapi.dto.UserResponse;
import com.vehicle.vehicleapi.dto.UserSummaryResponse;
import com.vehicle.vehicleapi.mapper.CarMapper;
import com.vehicle.vehicleapi.mapper.UserMapper;
import com.vehicle.vehicleapi.model.Car;
import com.vehicle.vehicleapi.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private CarMapper carMapper;

    // create user endpoint test case
    @Test
    void shouldCreateUserSuccessfully() throws Exception {

        CreateUserRequest request =
                new CreateUserRequest();

        request.setUsername("napoleon");
        request.setEmail("napoleon@email.com");
        request.setPassword("123456");
        request.setRole(Role.USER);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated());

        verify(service)
                .createUser(any(CreateUserRequest.class));
    }

    // create user validation test case
    @Test
    void shouldReturnBadRequestWhenUsernameMissing() throws Exception {

        CreateUserRequest request =
                new CreateUserRequest();

        request.setEmail("test@email.com");

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isBadRequest());
    }

    //get user by id test case endpoint
    @Test
    void shouldReturnUserById() throws Exception {

        User user = new User();

        user.setId(1L);
        user.setUsername("napoleon");

        UserResponse response =
                new UserResponse();

        response.setId(1L);
        response.setUsername("napoleon");

        when(service.getUserById(1L))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(response);

        mockMvc.perform(
                get("/users/1")
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$.data.id")
                                .value(1)
                );
    }

    // delete user endpoint test case
    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(
                delete("/users/1")
        )
                .andExpect(status().isOk());

        verify(service)
                .deleteUser(1L);
    }

    // update user test case
    @Test
    void shouldUpdateUser() throws Exception {

        UpdateUserRequest request =
                new UpdateUserRequest();

        request.setUsername("newname");

        mockMvc.perform(
                patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk());

        verify(service)
                .updateUser(
                        eq(1L),
                        any(UpdateUserRequest.class)
                );
    }

    // get all user testcase
    @Test
    void shouldReturnAllUsers() throws Exception {

        UserSummaryResponse user =
                new UserSummaryResponse(
                        1L,
                        "napoleon",
                        "test@email.com",
                        Role.USER,
                        2
                );

        Page<UserSummaryResponse> page =
                new PageImpl<>(
                        List.of(user)
                );

        when(service.getAllUser(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                get("/users")
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data.content[0].username")
                                .value("napoleon")
                );
    }

    // get user cars endpoint test case
    @Test
    void shouldReturnUserCars() throws Exception {

        Car car = new Car();

        car.setTicket(1L);
        car.setLicensePlate("ABC123");

        CarResponse response =
                new CarResponse();

        response.setTicket(1L);
        response.setLicensePlate("ABC123");

        Page<Car> cars =
                new PageImpl<>(
                        List.of(car)
                );

        when(service.getUserCar(
                eq(1L),
                any(Pageable.class)
        )).thenReturn(cars);

        when(carMapper.toResponse(car))
                .thenReturn(response);

        mockMvc.perform(
                get("/users/1/cars")
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.data.content[0].licensePlate")
                                .value("ABC123")
                );
    }

    // add car to user endpoint test case
    @Test
    void shouldAddCarToUser() throws Exception {

        CreateCarRequest request =
                new CreateCarRequest();

        request.setLicensePlate("ABC123");

        mockMvc.perform(
                post("/users/1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isCreated());

        verify(service)
                .addUserCar(
                        eq(1L),
                        any(CreateCarRequest.class)
                );
    }

    // update user car test case
    @Test
    void shouldUpdateUserCar() throws Exception {

        UpdateCarRequest request =
                new UpdateCarRequest();

        request.setBrand("Honda");

        mockMvc.perform(
                patch("/users/1/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(request)
                        )
        )
                .andExpect(status().isOk());

        verify(service)
                .updateUserCar(
                        eq(1L),
                        eq(1L),
                        any(UpdateCarRequest.class)
                );
    }

}
