package com.miguno.javadockerbuild;

/**
 * {@link Greeting} is resource representation class, from which the eventual
 * JSON response message is being derived.
 *
 * @param id The unique ID of the response.
 * @param name The name to be greeted.
 */
public record Greeting(long id, String name) { }
