package com.miguno.javadockerbuild;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/status")
public class StatusResource {

  private final Status status = new Status("idle");

  @GET
  public Status list() {
    return status;
  }
}
