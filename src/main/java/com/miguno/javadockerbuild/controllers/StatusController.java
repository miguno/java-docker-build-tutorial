package com.miguno.javadockerbuild.controllers;

import com.miguno.javadockerbuild.Status;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The only API endpoint exposed by this example application. */
@SuppressFBWarnings("SPRING_ENDPOINT")
@RestController
public class StatusController {

  private static final String template = "Hello, %s!";

  /**
   * Returns a greeting to the client.
   *
   * @param name The name to greet.
   * @return A personalized greeting.
   */
  @GetMapping("/status")
  public Status status(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new Status(String.format(template, name));
  }
}
