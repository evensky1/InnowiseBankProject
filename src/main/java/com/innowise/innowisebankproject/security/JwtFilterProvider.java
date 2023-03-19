package com.innowise.innowisebankproject.security;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
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

    @EJB
    private JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        var authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            var token = authHeader.substring("Bearer".length()).trim();

            jwtService.validateToken(token);
        } catch (JwtException | NullPointerException e) {
            containerRequestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
        }
    }
}
