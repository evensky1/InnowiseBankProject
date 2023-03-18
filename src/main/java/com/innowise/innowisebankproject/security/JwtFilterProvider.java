package com.innowise.innowisebankproject.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Provider
@JwtFilter
@Priority(Priorities.AUTHENTICATION)
public class JwtFilterProvider implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        var authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            var token = authHeader.substring("Bearer".length()).trim();

            var key = System.getenv("JWT_KEY").getBytes();
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        } catch (JwtException | NullPointerException e) {
            containerRequestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
        }
    }
}
