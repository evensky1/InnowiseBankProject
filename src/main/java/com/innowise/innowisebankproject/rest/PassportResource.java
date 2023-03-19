package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.security.JwtFilter;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.PassportService;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/v1/passports")
public class PassportResource {

    @EJB
    private PassportService passportService;
    @EJB
    private JwtService jwtService;
    @EJB
    private UserService userService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response getById(@PathParam("id") Long id) {
        return Response.ok()
            .entity(passportService.getById(id))
            .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response attachPassport(Passport passport,
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {

        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);

        var savedPassport = passportService.save(passport);
        userService.attachPassport(email, savedPassport);

        return Response.noContent().build();
    }
}
