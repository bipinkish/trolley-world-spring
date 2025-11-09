package com.bipinkish.store.services;

import com.bipinkish.store.config.JwtConfig;
import com.bipinkish.store.entities.Role;
import com.bipinkish.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        var tokenExpirationDate = new Date(System.currentTimeMillis() + jwtConfig.getAccessTokenExpiration() * 1000L);
        return generateToken(user, tokenExpirationDate);
    }

    public Jwt generateRefreshToken(User user) {
        var tokenExpirationDate = new Date(System.currentTimeMillis() + jwtConfig.getRefreshTokenExpiration() * 1000L);
        user.setRefreshTokenExpiration(tokenExpirationDate);
        return generateToken(user, tokenExpirationDate);
    }

    private Jwt generateToken(User user, Date tokenExpirationDate) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .build();
        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt parse(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(jwtConfig.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }

    }

}
