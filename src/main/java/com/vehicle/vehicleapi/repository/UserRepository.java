package com.vehicle.vehicleapi.repository;

import com.vehicle.vehicleapi.dto.UserSummaryResponse;
import com.vehicle.vehicleapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);

    Optional<User> findByUsernameOrEmail(
        String username,
        String email
    );

    boolean existsByEmailAndIdNot(
        String email,
        Long id
    );

    boolean existsByUsernameAndIdNot(
        String username,
        Long id
    );

    @Query("""
        SELECT new com.vehicle.vehicleapi.dto.UserSummaryResponse(
            u.id,
            u.username,
            u.email,
            u.role,
            SIZE(u.cars)
        )
        FROM User u
    """)
    Page<UserSummaryResponse> findAllUserResponses(Pageable pageable);
}
