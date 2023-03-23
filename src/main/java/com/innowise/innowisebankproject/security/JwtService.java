package com.innowise.innowisebankproject.security;

import com.innowise.innowisebankproject.entity.Role;
import com.innowise.innowisebankproject.entity.RoleName;
import com.innowise.innowisebankproject.entity.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

@Stateless
public class JwtService {

    public String generateJwt(User user) {
        var decodedKey = System.getenv("JWT_KEY").getBytes();
        var key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.builder()
            .claim("email", user.getEmail())
            .claim("roles", user.getRoles().stream().map(Role::getName).toList())
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

    public List<RoleName> fetchRoles(String token) {
        var key = System.getenv("JWT_KEY").getBytes();

        var roles = (List<String>) Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("roles");

        return roles.stream().map(RoleName::valueOf).toList();
    }
}
