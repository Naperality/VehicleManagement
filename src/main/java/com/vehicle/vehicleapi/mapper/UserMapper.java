package com.vehicle.vehicleapi.mapper;

import org.springframework.stereotype.Component;

import com.vehicle.vehicleapi.dto.UserResponse;
import com.vehicle.vehicleapi.dto.UserCarResponse;
import com.vehicle.vehicleapi.model.User;

import java.util.List;

@Component
public class UserMapper {

    private final UserCarMapper userCarMapper;

    public UserMapper(UserCarMapper userCarMapper){
        this.userCarMapper = userCarMapper;
    }

    public UserResponse toResponse(User user){

        List<UserCarResponse> cars = user.getCars() == null ? List.of():
                                        user.getCars()
                                        .stream()
                                        .map(userCarMapper::toResponse)
                                        .toList();

        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getCars() == null
                ? 0
                : user.getCars().size(),
            cars
        );
    }
}