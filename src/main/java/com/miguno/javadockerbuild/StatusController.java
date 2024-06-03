package com.miguno.javadockerbuild;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The only API endpoint exposed by this example application. */
@SuppressFBWarnings("SPRING_ENDPOINT")
@RestController
public class StatusController {

  private static final String template = "Hello, %s!";

  @GetMapping("/status")
  public Status status(@RequestParam(value = "name", defaultValue = "World") String name) {
    return new Status(String.format(template, name));
  }
}
