package com.vehicle.vehicleapi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.exception.UserNotFoundException;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{
    
    private final UserRepository repository;

    public DatabaseUserDetailsService(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(
        String username
    )throws UsernameNotFoundException{
        User user = repository
                .findByUsername(username)
                .orElseThrow(
                    () -> new UserNotFoundException(
                        "User Not Found"
                    )
                );
        return new CustomerUserDetails(user);
    }
}
