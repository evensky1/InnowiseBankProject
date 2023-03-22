package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.User;
import com.innowise.innowisebankproject.service.AuthService;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/v1/auth")
public class AuthController {

    @EJB
    private UserService userService;
    @EJB
    private AuthService authService;

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

        var jwt = authService.authorize(user);

        return Response.noContent()
            .header("Authorization", "Bearer " + jwt)
            .build();
    }

}
