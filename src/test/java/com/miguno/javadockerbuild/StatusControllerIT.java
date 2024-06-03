package com.miguno.javadockerbuild;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

/** An example integration test for the API endpoint `/status`. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatusControllerIT {

  @Autowired private TestRestTemplate template;

  @Test
  public void getStatus() throws Exception {
    ResponseEntity<String> response = template.getForEntity("/status", String.class);
    String expectedJson = """
           {"status":"Hello, World!"}
        """;
    JSONAssert.assertEquals(expectedJson, response.getBody(), JSONCompareMode.STRICT);
  }
}
