package com.example.springboottesting.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @param customer it should work without it @JsonProperty, shouldn't it?
 */
public record CustomerRegistrationRequest(@JsonProperty("customer") Customer customer) {}
