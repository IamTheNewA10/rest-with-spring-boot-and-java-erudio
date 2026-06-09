package br.com.het.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.het.exception.ResourceNotFoundException;
import br.com.het.model.Person;
import br.com.het.repository.PersonRepository;

@Service
public class PersonService {

  private Logger logger = Logger.getLogger(PersonService.class.getName());

  @Autowired
  PersonRepository repository;

  public List<Person> findAll() {
    logger.info("Finding all People!");
    return repository.findAll();
  }

  public Person findById(Long id) {
    logger.info("Finding one Person!");
    return repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
  }

  public Person create(Person person) {
    logger.info("Creating new person!");
    return repository.save(person);
  }

  public Person update(Person person) {
    logger.info("updating one person");
    Person entity = repository.findById(person.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
    entity.setFirstName(person.getFirstName());
    entity.setLastName(person.getLastName());
    entity.setAdress(person.getAdress());
    entity.setGender(person.getGender());

    return repository.save(entity);
  }

  public void delete(Long id) {
    logger.info("deleting one person");
    Person entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
    repository.delete(entity);
  }
}
