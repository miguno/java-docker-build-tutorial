package com.miguno.javadockerbuild;

import com.miguno.javadockerbuild.controllers.WelcomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmokeTest {

  @Autowired private WelcomeController controller;

  @Test
  void verifyThatApplicationContextLoads() {
    // Will fail if the application context cannot start.
  }
}
