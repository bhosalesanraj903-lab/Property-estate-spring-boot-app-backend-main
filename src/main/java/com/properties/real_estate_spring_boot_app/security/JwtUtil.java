package com.properties.real_estate_spring_boot_app.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "THIS_IS_A_VERY_LONG_SECRET_KEY_FOR_HS256_SIGNING_THIS_IS_SAFE";

    private static final long EXPIRATION = 15 * 60 * 1000; // 15 minutes

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // ✅ GENERATE JWT TOKEN
    public String generateToken(Long userId, String email, String role) {

        // ✅ ENSURE SPRING-COMPATIBLE ROLE
//        String springRole = role.startsWith("ROLE_")
//                ? role
//                : "ROLE_" + role;

        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ VALIDATE & PARSE TOKEN
    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String generateRefreshToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
