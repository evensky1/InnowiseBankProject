package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.entity.Account;
import com.innowise.innowisebankproject.security.JwtFilter;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.AccountService;
import com.innowise.innowisebankproject.service.UserService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/v1/accounts")
public class AccountResource {

    @EJB
    private AccountService accountService;
    @EJB
    private UserService userService;
    @EJB
    private JwtService jwtService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response createAccount(Account account,
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {
        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);

        var currentUser = userService.findUserByEmail(email);

        return Response.status(Status.CREATED)
            .entity(accountService.createAccount(account, currentUser.getId()))
            .build();
    }

    @GET
    @Path("/user-attached")
    @Produces(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response getUserAttachedAccounts(
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {

        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);
        var user = userService.findUserByEmail(email);

        var accounts = accountService.getAccountsByUserId(user.getId());

        return Response.ok()
            .entity(accounts)
            .build();
    }
}
