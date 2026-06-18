package br.com.het.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.controllers.docs.PersonControllerDocs;
import br.com.het.data.dto.PersonDTO;
import br.com.het.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

  @Autowired
  private PersonService service;

  @Override
  @GetMapping(produces = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE })
  public List<PersonDTO> findAll() {
    return service.findAll();
  }

  @Override
  @GetMapping(value = "/{id}", produces = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE })
  // @CrossOrigin(origins = "http://localhost:8080")
  public PersonDTO findById(@PathVariable("id") Long id) {
    return service.findById(id);
  }

  @Override
  @PostMapping(consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE }, produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE })
  // @CrossOrigin(origins = "http://localhost:8080")
  public PersonDTO create(@RequestBody PersonDTO PersonDTO) {
    return service.create(PersonDTO);
  }

  @Override
  @PutMapping(consumes = {
      MediaType.APPLICATION_JSON_VALUE,
      MediaType.APPLICATION_XML_VALUE,
      MediaType.APPLICATION_YAML_VALUE }, produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_YAML_VALUE })
  public PersonDTO update(@RequestBody PersonDTO PersonDTO) {
    return service.update(PersonDTO);
  }

  @Override
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
