package br.com.het.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.het.data.dto.PersonDTO;
import br.com.het.service.PersonService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/person")
public class PersonController {

  @Autowired
  private PersonService service;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<PersonDTO> findAll() {
    return service.findAll();
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PersonDTO findById(@PathVariable("id") Long id) {
    return service.findById(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public PersonDTO crete(@RequestBody PersonDTO PersonDTO) {
    return service.create(PersonDTO);
  }

  @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public PersonDTO update(@RequestBody PersonDTO PersonDTO) {
    return service.update(PersonDTO);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
