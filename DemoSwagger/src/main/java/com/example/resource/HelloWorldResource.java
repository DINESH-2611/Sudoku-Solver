package com.example.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Returns a hello world message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returns hello message")
    })
    public String sayHello() {
        return "{\"message\": \"Hello, World!\"}";
    }
}
