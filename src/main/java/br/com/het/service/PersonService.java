package br.com.het.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.het.data.dto.PersonDTO;
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
    return parseListObject(repository.findAll(), PersonDTO.class);
  }

  public PersonDTO findById(Long id) {
    logger.info("Finding one Person!");
    var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    return parseObject(entity, PersonDTO.class);
  }

  public PersonDTO create(PersonDTO person) {
    logger.info("Creating new person!");
    var entity = parseObject(person, Person.class);
    return parseObject(repository.save(entity), PersonDTO.class);
  }

  public PersonDTO update(PersonDTO person) {
    logger.info("updating one person");
    Person entity = repository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    entity.setFirstName(person.getFirstName());
    entity.setLastName(person.getLastName());
    entity.setAdress(person.getAdress());
    entity.setGender(person.getGender());

    return parseObject(repository.save(entity), PersonDTO.class);
  }

  public void delete(Long id) {
    logger.info("deleting one person");
    Person entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    repository.delete(entity);
  }
}
