package com.miguno.javadockerbuild;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Example unit tests for the API endpoint `/status`. */
@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  public void getStatus() throws Exception {
    String expectedJson = """
                {"status":"Hello, World!"}
        """;

    mvc.perform(MockMvcRequestBuilders.get("/status").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(expectedJson));
  }
}
