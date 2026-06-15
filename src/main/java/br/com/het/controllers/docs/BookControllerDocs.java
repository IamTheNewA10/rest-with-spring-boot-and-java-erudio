package br.com.het.controllers.docs;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.het.data.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface BookControllerDocs {
  @Operation(summary = "Find All Books", description = "Finds All Books", tags = { "Books" }, responses = {
      @ApiResponse(description = "Success", responseCode = "200", content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BookDTO.class)))
      }),
      @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
  })
  List<BookDTO> findAll();

  @Operation(summary = "Finds a Book", description = "Find a specific Book by your ID", tags = {
      "People" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = BookDTO.class))),
          @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  BookDTO findById(@PathVariable("id") Long id);

  @Operation(summary = "Adds a new Book", description = "Adds a new Book by passing in a JSON, XML or YML representation of the Book.", tags = {
      "People" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = BookDTO.class))),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  BookDTO create(@RequestBody BookDTO Book);

  @Operation(summary = "Updates a Book's information", description = "Updates a Book's information by passing in a JSON, XML or YML representation of the updated Book.", tags = {
      "People" }, responses = {
          @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = BookDTO.class))),
          @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  BookDTO update(@RequestBody BookDTO Book);

  @Operation(summary = "Deletes a Book", description = "Deletes a specific Book by their ID", tags = {
      "People" }, responses = {
          @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
      })
  ResponseEntity<?> delete(@PathVariable("id") Long id);
}
