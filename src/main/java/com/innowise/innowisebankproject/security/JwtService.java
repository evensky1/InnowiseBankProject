package com.innowise.innowisebankproject.security;

import com.innowise.innowisebankproject.entity.Role;
import com.innowise.innowisebankproject.entity.RoleName;
import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.util.JwtProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.ejb.Stateless;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

@Stateless
public class JwtService {

    public String generateJwt(User user) {
        var decodedKey = System.getenv(JwtProperties.JWT_KEY).getBytes();
        var key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return Jwts.builder()
            .claim(JwtProperties.EMAIL_CLAIM, user.getEmail())
            .claim(JwtProperties.ROLE_CLAIM, user.getRoles().stream().map(Role::getName).toList())
            .signWith(key)
            .compact();
    }

    public void validateToken(String token) throws JwtException {

        var key = System.getenv(JwtProperties.JWT_KEY).getBytes();
        Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);
    }

    public String fetchEmail(String token) {
        var key = System.getenv(JwtProperties.JWT_KEY).getBytes();

        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(JwtProperties.EMAIL_CLAIM)
            .toString();
    }

    public List<RoleName> fetchRoles(String token) {
        var key = System.getenv(JwtProperties.JWT_KEY).getBytes();

        var roles = (List<String>) Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get(JwtProperties.ROLE_CLAIM);

        return roles.stream().map(RoleName::valueOf).toList();
    }
}
