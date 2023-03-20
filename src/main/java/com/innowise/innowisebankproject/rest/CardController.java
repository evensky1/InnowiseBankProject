package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.dto.AccountCardWrapper;
import com.innowise.innowisebankproject.security.JwtFilter;
import com.innowise.innowisebankproject.security.JwtService;
import com.innowise.innowisebankproject.service.AccountService;
import com.innowise.innowisebankproject.service.CardService;
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

@Path("/v1/cards")
public class CardController {

    @EJB
    private CardService cardService;
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
    public Response createCard(AccountCardWrapper accountCardWrapper,
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {

        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);

        var user = userService.findUserByEmail(email);
        var account =
            accountService.findByNum(accountCardWrapper.getAccount().getNumber());

        var card =
            cardService.createCard(accountCardWrapper.getCard(), account.getId(), user.getId());

        return Response.status(Status.CREATED)
            .entity(card)
            .build();
    }

    @GET
    @Path("/user-attached")
    @Produces(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response getUserAttachedCards(
        @HeaderParam(HttpHeaders.AUTHORIZATION) String jwt) {

        jwt = jwt.substring("Bearer".length()).trim();
        var email = jwtService.fetchEmail(jwt);
        var user = userService.findUserByEmail(email);

        var cards = cardService.getCardsByUserId(user.getId());

        return Response.ok()
            .entity(cards)
            .build();
    }
}
