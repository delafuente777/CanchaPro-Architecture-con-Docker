package com.canchapro.auth_service.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes()
        );
    }

    public String generateToken(
            UserDetails userDetails
    ) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(
                        getSigningKey()
                )
                .compact();
    }

    public String extractUsername(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(
                        (javax.crypto.SecretKey) getSigningKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        return extractUsername(token)
                .equals(userDetails.getUsername());
    }
}
