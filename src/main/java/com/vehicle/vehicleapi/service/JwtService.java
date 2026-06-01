package com.vehicle.vehicleapi.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vehicle.vehicleapi.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long expiration;
    
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private <T> T extractClaim(
        String token,
        Function<Claims, T> claimsResolver
    ){
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public <T> T extractCustomClaim(
        String token,
        String claimName,
        Class<T> type
    ){
        return extractClaim(
            token,
            claims -> claims.get(claimName, type)
        );
    }

    // generation of token
    public String generateToken(User user){
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole().name())
            .claim("userId", user.getId())
            .claim("email", user.getEmail())
            .issuedAt(new Date())
            .expiration(
                new Date(
                    System.currentTimeMillis()
                    + expiration
                )
            )
            .signWith(getSigningKey())
            .compact();
    }

    // extracting username via claims extract claims
    public String extractUsername(String token){
        return extractClaim(
            token,
            Claims::getSubject // because username is stored in subject
        );
    }

    // extracting roles
    public String extractRole(String token){
        return extractCustomClaim(
            token,
            "role",
            String.class
        );
    }

    // extracting expiration date using extract claims
    public Date extractExpiration(String token){
        return extractClaim(
            token,
            Claims::getExpiration // because expiration date stored 
        );
    }

    // extract userId
    public Long extractUserId(String token){
        return extractCustomClaim(
            token,
            "userId",
            Long.class
        );
    }

    //extract email
    public String extractEmail(String token){
        return extractCustomClaim(
            token,
            "email",
            String.class
        );
    }
    
    // check expiration
    private boolean isTokenExpired(String token){
        return extractExpiration(token)
            .before(new Date());
    }

    // token validation via expiration date extracted
    public boolean isTokenValid(
        String token,
        UserDetails userDetails
    ){
        String username =
            extractUsername(token);

        return username.equals(
                userDetails.getUsername()
            )
            && !isTokenExpired(token);
    }
}
