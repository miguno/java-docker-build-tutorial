package com.miguno;

import java.net.URI;

import jakarta.ws.rs.client.Client;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {

    private org.glassfish.grizzly.http.server.HttpServer server;
    private JerseyWebTarget target;

    @BeforeEach
    public void setUp() {
        server = startServer();
        ClientConfig config = new ClientConfig();
        Client client = JerseyClientBuilder.newClient(config);
        target = (JerseyWebTarget) client.target(App.BASE_URI);
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

    protected org.glassfish.grizzly.http.server.HttpServer startServer() {
        // Create a resource config that scans for JAX-RS resources and providers in com.miguno package
        final ResourceConfig rc = new ResourceConfig().packages("com.miguno");

        // Disable WADL feature
        rc.property("jersey.config.server.wadl.disableWadl", true);

        // Create and start a new instance of Grizzly HTTP server
        // Exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(App.BASE_URI), rc);
    }
}
