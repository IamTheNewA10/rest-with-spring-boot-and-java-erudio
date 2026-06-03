package br.com.het.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.het.model.Greeting;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GreetingController {

  private static final String template = "Hello, %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping("/greeting")
  public Greeting greeting(@RequestParam(value = "name", defaultValue = "Hello World") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }
}
