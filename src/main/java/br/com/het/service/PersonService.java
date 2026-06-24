package br.com.het.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.het.controllers.PersonController;
import br.com.het.data.dto.PersonDTO;
import br.com.het.exception.BadRequestException;
import br.com.het.exception.FileStorageException;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.exception.ResourceNotFoundException;
import br.com.het.file.exporter.MediaTypes;
import br.com.het.file.exporter.contract.FileExporter;
import br.com.het.file.exporter.factory.FileExporterFactory;
import br.com.het.file.importer.contract.FileImporter;
import br.com.het.file.importer.factory.FileImporterFactory;

import static br.com.het.mapper.ObjectMapper.parseListObject;
import static br.com.het.mapper.ObjectMapper.parseObject;
import br.com.het.model.Person;
import br.com.het.repository.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

  private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

  @Autowired
  PersonRepository repository;

  @Autowired
  FileImporterFactory importer;

  @Autowired
  FileExporterFactory exporter;

  @Autowired
  PagedResourcesAssembler<PersonDTO> assembler;

  public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
    logger.info("Finding all People!");

    var people = repository.findAll(pageable);
    return buildPageModel(pageable, people);
  }

  public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
    logger.info("Finding all People!");

    var people = repository.findPeopleByName(firstName, pageable);
    return buildPageModel(pageable, people);
  }

  public PersonDTO findById(Long id) {
    logger.info("Finding one Person!");
    var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    var dto = parseObject(entity, PersonDTO.class);
    addHateoasLinks(dto);
    return dto;
  }

  public Resource exportPage(Pageable pageable, String acceptHeader) {
    logger.info("Exporting a People Page!");

    var people = repository.findAll(pageable)
        .map(p -> parseObject(p, PersonDTO.class))
        .getContent();
    FileExporter exporter;
    try {
      exporter = this.exporter.geExporter(acceptHeader);
      return exporter.exportFile(people);
    } catch (Exception e) {
      throw new RuntimeException("Error During File Export!", e);
    }
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

  public List<PersonDTO> massCreation(MultipartFile file) {
    logger.info("Importing People from file");

    // verifica se o arquivo é nulo
    if (file.isEmpty())
      throw new BadRequestException("Please Set A Valid File!");

    try (InputStream inputStream = file.getInputStream()) {
      // Armazena o nome do arquivo verifica se o nome do arquivo é nulo
      String fileName = Optional.ofNullable(file.getOriginalFilename())
          .orElseThrow(() -> new BadRequestException("File Name Can Not Be Null"));
      // Verifica qual o formato do arquivo pelo nome e instancia a classe correta
      FileImporter importer = this.importer.getImporter(fileName);

      // importa o arquivo para uma lista de DTO dps converte para entidade e salva no
      // banco
      List<Person> entities = importer.importFile(inputStream).stream()
          .map(dto -> repository.save(parseObject(dto, Person.class)))
          .toList();

      // converte novamente para DTO ,adiciona os links e retorna a resposta ao
      // servidor
      return entities.stream().map(entity -> {
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
      }).toList();

    } catch (Exception e) {
      throw new FileStorageException("ERROR Processing File");
    }
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

  @Transactional
  public PersonDTO disablePerson(Long id) {
    logger.info("Disabling one person");
    repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    repository.disablePerson(id);

    var entity = repository.findById(id).get();
    var dto = parseObject(entity, PersonDTO.class);
    return dto;
  }

  private PagedModel<EntityModel<PersonDTO>> buildPageModel(Pageable pageable, Page<Person> people) {
    var peopleWithLinks = people.map(person -> {
      var dto = parseObject(person, PersonDTO.class);
      addHateoasLinks(dto);
      return dto;
    });

    Link findAllLink = WebMvcLinkBuilder.linkTo(
        WebMvcLinkBuilder.methodOn(PersonController.class)
            .findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                String.valueOf(pageable.getSort())))
        .withSelfRel();

    return assembler.toModel(peopleWithLinks, findAllLink);
  }

  private void addHateoasLinks(PersonDTO dto) {
    dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));

    dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));

    dto.add(
        linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withRel("findByName").withType("GET"));

    dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));

    dto.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("massCreation").withType("POST"));

    dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));

    dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));

    dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));

    dto.add(linkTo(methodOn(PersonController.class)
        .exportPage(1, 12, "asc", null))
        .withRel("exportPage")
        .withType("GET")
        .withTitle("Export People"));
  }

}
