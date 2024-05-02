package com.miguno.javadockerbuild;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// By default, @SpringBootTest does not start the web server, but instead sets
// up a mock environment for testing web endpoints.  Thus, this test covers
// almost but not entirely the full stack.
// https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing
@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDefaultResponse() throws Exception {
        this.mockMvc.perform(get("/greeting").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hello, World!"));
    }

    @Test
    void shouldReturnParameterizedResponse() throws Exception {
        var name = "Foo";
        this.mockMvc.perform(get("/greeting?name=" + name).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hello, " + name + "!"));
    }
}