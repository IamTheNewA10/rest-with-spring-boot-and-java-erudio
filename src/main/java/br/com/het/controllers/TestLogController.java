package br.com.het.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/test/v1")
public class TestLogController {

  private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

  @GetMapping("/test")
  public String testLog() {
    logger.debug("this is an debug log");
    logger.info("this is an info log");
    logger.warn("this is an warn log");
    logger.error("this is an error log");
    return "Logs generated sucessfully";
  }
}
