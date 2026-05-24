package com.example.JournalApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username){

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + expiration)
                )
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractUsername(String token){

        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(
            String token,
            String username){

        return extractUsername(token).equals(username);
    }
}