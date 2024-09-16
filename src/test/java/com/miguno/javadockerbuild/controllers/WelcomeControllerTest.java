package com.miguno.javadockerbuild.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Example unit tests for the API endpoint `/welcome`. */
@SpringBootTest
@AutoConfigureMockMvc
public class WelcomeControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  public void getWelcome() throws Exception {
    String expectedJson = """
                {"welcome":"Hello, World!"}
        """;

    mvc.perform(MockMvcRequestBuilders.get("/welcome").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }

  @Test
  public void getWelcomeWithPathVariable() throws Exception {
    String expectedJson = """
                {"welcome":"Hello, Gandalf!"}
        """;

    mvc.perform(MockMvcRequestBuilders.get("/welcome/Gandalf").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }
}
