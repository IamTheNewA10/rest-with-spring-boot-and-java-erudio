package br.com.het.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.data.dto.security.AccountCredentialsDTO;
import br.com.het.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  AuthService service;

  @Operation(summary = "Authenticates an User and Returns a Token")
  @PostMapping("/signin")
  public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials) {
    // # verfica se o username e password estao vazios
    if (credentialsIsInvalid(credentials))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");

    // chamando o service
    var token = service.signIn(credentials);

    if (token == null)
      ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");
    return ResponseEntity.ok().body(token);
  }

  // #
  private boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
    return credentials == null || StringUtils.isAllBlank(credentials.getPassword())
        || StringUtils.isBlank(credentials.getUserName());
  }
}
