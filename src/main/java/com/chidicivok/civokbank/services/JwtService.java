package com.chidicivok.civokbank.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    // reference the secret key in application properties
    @Value("${jwt.secret}")
    private String secretKey;

    // convert the secret key to bytes
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // generate token
    public String generateToken(String email) {
        return Jwts.builder().subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSigningKey())
                .issuer("civok-bank-nigeria-ltd")
                .audience()
                .add("civok-api")
                .and()
                .id(UUID.randomUUID().toString())
                .compact();
    }

    // extract claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    // extract email
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // extract expiration date
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // extract issuer
    private String extractIssuer(String token) {
        return extractAllClaims(token).getIssuer();
    }

    public String extractJwtId(String token) {
        return extractAllClaims(token).getId();
    }

    // is token expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // is token valid
    public boolean isTokenValid(String token, UserDetails userDetails) {

        String email = extractEmail(token);

        return email.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
