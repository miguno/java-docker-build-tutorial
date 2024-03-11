package com.miguno;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class App {

    protected static final String BASE_URI = "http://0.0.0.0:8123/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     */
    protected static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers in com.miguno package
        final ResourceConfig rc = new ResourceConfig().packages("com.miguno");

        // disable WADL
        rc.property("jersey.config.server.wadl.disableWadl", true);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();
        if (server.isStarted()) {
            System.out.println(String.format("Endpoint is available at %sstatus", BASE_URI));
        }
    }
}

