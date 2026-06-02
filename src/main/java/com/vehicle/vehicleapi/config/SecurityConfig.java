package com.vehicle.vehicleapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vehicle.vehicleapi.model.Role;
import com.vehicle.vehicleapi.model.User;
import com.vehicle.vehicleapi.repository.UserRepository;
import com.vehicle.vehicleapi.security.DatabaseUserDetailsService;
import com.vehicle.vehicleapi.security.JwtAuthenticationFilter;

// import org.springframework.security.core.userdetails.User;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final DatabaseUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
        DatabaseUserDetailsService userDetailsService,
        JwtAuthenticationFilter jwtAuthenticationFilter
    ){
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
        HttpSecurity http
    )throws Exception{
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers(HttpMethod.POST, 
                    "/auth/register",
                    "/auth/login")
                .permitAll()

                .anyRequest()
                .authenticated()
            )
            // .httpBasic(Customizer.withDefaults());
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(
        PasswordEncoder encoder
    ){
        DaoAuthenticationProvider provider = 
            new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(
            encoder
        );

        return provider;
    }

    // creating a admin user for testing
    // @Bean
    // UserDetailsService userDetailsService(
    //     PasswordEncoder encoder
    // ){

    //     UserDetails admin =
    //         User.withUsername("admin")
    //             .password(
    //                 encoder.encode("password")
    //             )
    //             .roles("ADMIN")
    //             .build();

    //     return new InMemoryUserDetailsManager(admin);
    // }

    // creating encryption for passwords
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    CommandLineRunner seedAdmin(
        UserRepository repository,
        PasswordEncoder encoder
    ){
        return args -> {

            if(repository.findByUsername("admin").isEmpty()){

                User admin = new User();

                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(
                    encoder.encode("wrong")
                );
                admin.setRole(Role.ADMIN);

                repository.save(admin);
            }
        };
    }
}
