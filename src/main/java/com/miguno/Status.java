package com.miguno;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("status")
public class Status {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String statusInformation() {
        return "{\"status\": \"idle\"}\n";
    }
}
