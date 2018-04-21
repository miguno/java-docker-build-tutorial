package com.miguno;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("status")
public class Status {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String statusInformation() {
    return "{\"status\": \"idle\"}\n";
  }
}