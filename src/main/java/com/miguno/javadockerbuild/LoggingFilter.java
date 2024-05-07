package com.miguno.javadockerbuild;

import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/** Controls how incoming HTTP requests to the API endpoints are being logged. */
@Provider
public class LoggingFilter implements ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

  @Context UriInfo info;

  @Context HttpServerRequest request;

  @Override
  public void filter(ContainerRequestContext context) {
    // Log incoming HTTP requests
    final String method = context.getMethod();
    final String path = info.getPath();
    final String address = request.remoteAddress().toString();
    LOG.infof("Request %s %s from IP %s", method, path, address);
  }
}
