package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.Passport;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.PassportService;
import com.innowise.innowisebankproject.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("/passport")
public class PassportResource {

    @EJB
    private PassportService passportService;
    @EJB
    private JwtService jwtService;
    @EJB
    private PersonService personService;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok()
            .entity(passportService.getById(id))
            .build();
    }

    @POST
    public Response attachPassport(Passport passport,
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {

        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);

        var savedPassport = passportService.save(passport);
        personService.attachPassport(email, savedPassport);

        return Response.noContent().build();
    }
}
