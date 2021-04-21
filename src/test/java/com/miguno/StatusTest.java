package com.miguno;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

  private HttpServer server;
  private WebTarget target;

  @BeforeEach
  public void setUp() {
    server = App.startServer();
    Client c = ClientBuilder.newClient();
    target = c.target(App.BASE_URI);
  }

  @AfterEach
  public void tearDown() {
    server.shutdownNow();
  }

  @Test
  public void shouldReceiveIdleStatus() {
    String responseMsg = target.path("status").request().get(String.class);
    assertEquals("{\"status\": \"idle\"}\n", responseMsg);
  }
}
