package com.miguno.javadockerbuild.controllers;

import com.miguno.javadockerbuild.models.Welcome;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The API endpoint exposed by this example application. */
@SuppressFBWarnings("SPRING_ENDPOINT")
@RestController
public class WelcomeController {

  private static final String template = "Hello, %s!";

  /**
   * Returns a welcome message to the client.
   *
   * @param name The name to greet.
   * @return A personalized welcome message.
   */
  @GetMapping("/welcome")
  public Welcome welcome(@RequestParam(value = "name", defaultValue = "World") String name) {
    // Note: If you make changes to the URL path, remember to update AppSecurityConfiguration.
    return new Welcome(String.format(template, name));
  }

  /**
   * Returns a welcome message to the client.
   *
   * @param name The name to greet.
   * @return A personalized welcome message.
   */
  @GetMapping("/welcome/{name}")
  public Welcome welcomeWithPathVariable(@PathVariable(value = "name") String name) {
    // Note: If you make changes to the URL path, remember to update AppSecurityConfiguration.
    return new Welcome(String.format(template, name));
  }
}
