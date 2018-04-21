package com.miguno;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class StatusTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() {
    server = App.startServer();
    Client c = ClientBuilder.newClient();
    target = c.target(App.BASE_URI);
  }

  @After
  public void tearDown() {
    server.shutdownNow();
  }

  @Test
  public void shouldReceiveIdleStatus() {
    String responseMsg = target.path("status").request().get(String.class);
    assertEquals("{\"status\": \"idle\"}\n", responseMsg);
  }
}
