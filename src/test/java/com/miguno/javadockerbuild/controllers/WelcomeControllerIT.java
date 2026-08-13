package com.miguno.javadockerbuild.controllers;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

/** An example integration test for the API endpoint `/welcome`. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Since Spring Boot 4, `RestTestClient` replaces `TestRestTemplate`, and the
// client must be requested explicitly.
@AutoConfigureRestTestClient
public class WelcomeControllerIT {

  @Autowired private RestTestClient client;

  @Test
  public void welcome() throws Exception {
    String actualJson =
        client
            .get()
            .uri("/welcome")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .returnResult()
            .getResponseBody();
    String expectedJson =
        """
           {"welcome":"Hello, World!"}
        """;
    JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.STRICT);
  }
}
