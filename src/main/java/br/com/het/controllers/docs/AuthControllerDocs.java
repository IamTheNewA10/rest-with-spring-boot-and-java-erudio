package br.com.het.controllers.docs;

import org.springframework.http.ResponseEntity;

import br.com.het.data.dto.PersonDTO;
import br.com.het.data.dto.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AuthControllerDocs {

  @Operation(summary = "Authenticates an User and Returns a Token", description = "Auhtenticates an User by Getting the Credentials via JSON, XML or YML and Returns a Valid Token.", tags = {
      "Authentication Endpoint" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  ResponseEntity<?> signIn(AccountCredentialsDTO credentials);

  @Operation(summary = "Refreshes the Token of an Authenticated User and Returns a new Token", description = "Reads a Token of an already Authenticated User and generates a new token in order to Refresh the Old Token", tags = {
      "Authentication Endpoint" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  ResponseEntity<?> refresh(String userName,
      String refreshToken);

  @Operation(summary = "Creates a New User", description = "Adds a new User by passing in a JSON, XML or YML representation of the User.", tags = {
      "Authentication Endpoint" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  AccountCredentialsDTO create(AccountCredentialsDTO credentials);

}