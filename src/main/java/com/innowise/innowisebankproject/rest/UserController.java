package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.RoleName;
import com.innowise.innowisebankproject.security.JwtFilter;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/users")
public class UserController {

    @EJB
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response getAllUsers() {

        return Response.ok()
            .entity(userService.findAll())
            .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JwtFilter(rolesAllowed = {RoleName.ADMIN})
    public Response getUserById(@PathParam("id") Long id) {

        return Response.ok()
            .entity(userService.findUserById(id))
            .build();
    }
}