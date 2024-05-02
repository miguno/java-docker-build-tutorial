package com.miguno.javadockerbuild;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// The @RestController annotation marks this class as a controller, where every
// method returns a domain object instead of a view. It is shorthand for
// including both @Controller and @ResponseBody.
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        // The new Greeting instance is automatically converted to JSON via
        // Spring’s HTTP message converter support (which uses Jackson 2).
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}