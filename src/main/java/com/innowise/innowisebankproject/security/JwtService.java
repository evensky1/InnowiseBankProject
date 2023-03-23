package com.innowise.innowisebankproject.security;

import com.innowise.innowisebankproject.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import javax.crypto.spec.SecretKeySpec;

@Stateless
public class JwtService {

    public String generateJwt(User user) {
        var decodedKey = System.getenv("JWT_KEY").getBytes();
        var key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.builder()
            .claim("email", user.getEmail())
            .signWith(key)
            .compact();
    }

    public void validateToken(String token) throws JwtException {

        var key = System.getenv("JWT_KEY").getBytes();
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }

    public String fetchEmail(String token) {
        var key = System.getenv("JWT_KEY").getBytes();

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("email")
            .toString();
    }
}
