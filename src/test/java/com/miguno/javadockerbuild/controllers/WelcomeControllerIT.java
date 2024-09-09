package com.miguno.javadockerbuild.controllers;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

/** An example integration test for the API endpoint `/welcome`. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WelcomeControllerIT {

  @Autowired private TestRestTemplate template;

  @Test
  public void welcome() throws Exception {
    ResponseEntity<String> response = template.getForEntity("/welcome", String.class);
    String expectedJson = """
           {"welcome":"Hello, World!"}
        """;
    JSONAssert.assertEquals(expectedJson, response.getBody(), JSONCompareMode.STRICT);
  }
}
