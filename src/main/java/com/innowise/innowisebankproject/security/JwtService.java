package com.innowise.innowisebankproject.security;

import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import javax.crypto.spec.SecretKeySpec;

@Stateless
public class JwtService {

    public String generateJwt(String email) {
        var decodedKey = System.getenv("JWT_KEY").getBytes();
        var key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.builder()
            .setSubject(email)
            .signWith(key)
            .compact();
    }
}
