package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/persons")
public class PersonResource {

    @EJB
    private PersonService personService;

    @GET
    @Produces("application/json")
    public Response getAllPersons() {

        return Response.ok()
            .entity(personService.findAllPersons())
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
    public Response getPersonById(@PathParam("id") Long id) {

        return Response.ok()
            .entity(personService.findPersonById(id))
            .build();
    }
}