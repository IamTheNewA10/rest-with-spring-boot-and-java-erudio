package br.com.het.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.het.controllers.PersonController;
import br.com.het.data.dto.PersonDTO;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.exception.ResourceNotFoundException;
import static br.com.het.mapper.ObjectMapper.parseListObject;
import static br.com.het.mapper.ObjectMapper.parseObject;
import br.com.het.model.Person;
import br.com.het.repository.PersonRepository;

@Service
public class PersonService {

  private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

  @Autowired
  PersonRepository repository;

  public List<PersonDTO> findAll() {
    logger.info("Finding all People!");
    var persons = parseListObject(repository.findAll(), PersonDTO.class);
    persons.forEach(this::addHateoasLinks); // <- pra cada objeto que vier adiciornar os links relacionados (forma mais
                                            // enxuta possivel)
    return persons;
  }

  public PersonDTO findById(Long id) {
    logger.info("Finding one Person!");
    var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    var dto = parseObject(entity, PersonDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public PersonDTO create(PersonDTO person) {

    if (person == null)
      throw new RequiredObjectIsNullException();

    logger.info("Creating new person!");
    var entity = parseObject(person, Person.class);
    var dto = parseObject(repository.save(entity), PersonDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public PersonDTO update(PersonDTO person) {

    if (person == null)
      throw new RequiredObjectIsNullException();

    logger.info("updating one person");
    Person entity = repository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    entity.setFirstName(person.getFirstName());
    entity.setLastName(person.getLastName());
    entity.setAdress(person.getAdress());
    entity.setGender(person.getGender());

    var dto = parseObject(repository.save(entity), PersonDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public void delete(Long id) {
    logger.info("deleting one person");
    Person entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    repository.delete(entity);
  }

  private void addHateoasLinks(PersonDTO dto) {
    dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
    dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
    dto.add(linkTo(methodOn(PersonController.class).crete(dto)).withRel("create").withType("POST"));
    dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
    dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
  }

}
