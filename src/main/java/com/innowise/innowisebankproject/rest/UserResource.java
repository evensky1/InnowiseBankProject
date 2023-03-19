package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @EJB
    private UserService userService;

    @GET
    @Produces("application/json")
    public Response getAllUsers() {

        return Response.ok()
            .entity(userService.findAll())
            .build();
    }

    @GET
    @Path("/greet")
    public Response sayHi() {
        return Response.ok()
            .entity("Hello")
            .build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getUserById(@PathParam("id") Long id) {

        return Response.ok()
            .entity(userService.findUserById(id))
            .build();
    }
}