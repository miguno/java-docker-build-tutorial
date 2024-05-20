package com.miguno.javadockerbuild;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTest {

  @Autowired private StatusController controller;

  @Test
  void verifyThatApplicationContextLoads() {
    // Will fail if the application context cannot start.
  }
}
