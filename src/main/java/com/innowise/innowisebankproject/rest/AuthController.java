package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.Person;
import com.innowise.innowisebankproject.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
public class AuthController {

    @EJB
    private PersonService personService;

    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(Person person) {

        return Response.status(Status.CREATED)
            .entity(personService.savePerson(person))
            .build();
    }

    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(Person person) {

        var jwt = personService.authorizePerson(person);

        return Response.noContent()
            .header("Authorization", "Bearer " + jwt)
            .build();
    }

}
