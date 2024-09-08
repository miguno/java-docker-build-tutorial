package com.miguno.javadockerbuild;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** An example application that exposes an HTTP endpoint. */
@SpringBootApplication
@EnableAdminServer
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
