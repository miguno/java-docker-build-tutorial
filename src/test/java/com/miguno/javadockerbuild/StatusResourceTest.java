package com.miguno.javadockerbuild;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StatusResourceTest {

    @Test
    public void verifyList() {
        given()
                .when().get("/status")
                .then()
                .statusCode(200)
                .body("status", is("idle"));
    }

}