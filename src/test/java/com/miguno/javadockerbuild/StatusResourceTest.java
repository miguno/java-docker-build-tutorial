package com.miguno.javadockerbuild;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/** An example unit test for the API endpoint /status. */
@QuarkusTest
public class StatusResourceTest {

  @Test
  public void verifyList() {
    given().when().get("/status").then().statusCode(200).body("status", is("idle"));
  }
}
