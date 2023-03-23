package com.innowise.innowisebankproject.security;

import com.innowise.innowisebankproject.entity.RoleName;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.util.stream.Stream;

@Provider
@JwtFilter
@Priority(Priorities.AUTHENTICATION)
public class JwtFilterProvider implements ContainerRequestFilter {

    @EJB
    private JwtService jwtService;
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {

        var authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        try {
            var token = authHeader.substring("Bearer".length()).trim();
            jwtService.validateToken(token);

            if (!checkForPermissions(token)) {
                containerRequestContext.abortWith(Response.status(Status.FORBIDDEN).build());
            }
        } catch (JwtException | NullPointerException e) {
            containerRequestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
        }
    }

    private boolean checkForPermissions(String token) {
        var method = resourceInfo.getResourceMethod();
        if (method == null) {
            return false;
        }

        var jwtContext = method.getAnnotation(JwtFilter.class);
        var allowedRoles = jwtContext.rolesAllowed();

        if (allowedRoles[0] == RoleName.ALL) {
            return true;
        }

        var userRoles = jwtService.fetchRoles(token);

        return Stream.of(allowedRoles).anyMatch(userRoles::contains);
    }
}
