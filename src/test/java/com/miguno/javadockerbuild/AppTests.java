package com.miguno.javadockerbuild;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// The @SpringBootTest annotation tells Spring Boot to look for a main
// configuration class (one with @SpringBootApplication, for instance)
// and use that to start a Spring application context.
@SpringBootTest
class AppTests {

	@Test
	void contextLoads() {
		// This test is a simple sanity check test that will fail if the
		// application context cannot start.
	}

}
