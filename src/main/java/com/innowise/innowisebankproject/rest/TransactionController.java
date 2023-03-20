package com.innowise.innowisebankproject.rest;

import com.innowise.innowisebankproject.dto.AccountTransactionRequest;
import com.innowise.innowisebankproject.security.JwtFilter;
import com.innowise.innowisebankproject.service.TransactionService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/v1/transaction")
public class TransactionController {

    @EJB
    private TransactionService transactionService;

    @POST
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JwtFilter
    public Response makeAccountTransaction(AccountTransactionRequest request) {
        transactionService.makeAccountTransaction(
            request.getDestAccountNum(),
            request.getAccountId(),
            request.getAmount(),
            request.getCurrencies());

        return Response.status(Status.CREATED).build();
    }
}
