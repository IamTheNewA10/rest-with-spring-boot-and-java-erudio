package br.com.het.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.controllers.docs.AuthControllerDocs;
import br.com.het.data.dto.security.AccountCredentialsDTO;
import br.com.het.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint", description = "Endpoints to Manage Authentication")
public class AuthController implements AuthControllerDocs {

  @Autowired
  AuthService service;

  @Override
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

  @Override
  @PutMapping("/refresh/{userName}")
  public ResponseEntity<?> refresh(@PathVariable("userName") String userName,
      @RequestHeader("Authorization") String refreshToken) {
    // # verfica se o username e password estao vazios
    if (parametersAreInvalid(userName, refreshToken))
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");

    // chamando o service
    var token = service.refresh(userName, refreshToken);

    if (token == null)
      ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request");
    return ResponseEntity.ok().body(token);
  }

  @Override
  @PostMapping(value = "/createUser", consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE }, produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE })
  public AccountCredentialsDTO create(@RequestBody AccountCredentialsDTO credentials) {
    return service.create(credentials);
  }

  private boolean parametersAreInvalid(String userName, String refreshToken) {
    return StringUtils.isBlank(userName) || StringUtils.isBlank(refreshToken);
  }

  // #
  private boolean credentialsIsInvalid(AccountCredentialsDTO credentials) {
    return credentials == null || StringUtils.isAllBlank(credentials.getPassword())
        || StringUtils.isBlank(credentials.getUserName());
  }
}
