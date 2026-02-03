package com.foodcloud.gateway.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.secret:my-super-secret-key-at-least-32-characters}")
    private String secretKey;

    @PostMapping("/token")
    public Map<String, String> generateToken(@RequestBody TokenRequest request) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .subject(request.userId())
                .claim("role", request.role())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 час
                .signWith(key)
                .compact();

        return Map.of("token", token);
    }

    record TokenRequest(String userId, String role) {}
}
