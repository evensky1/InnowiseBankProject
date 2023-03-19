package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
public class AuthController {

    @EJB
    private UserService userService;

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {

        return Response.status(Status.CREATED)
            .entity(userService.save(user))
            .build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(User user) {

        var jwt = userService.authorize(user);

        return Response.noContent()
            .header("Authorization", "Bearer " + jwt)
            .build();
    }

}
