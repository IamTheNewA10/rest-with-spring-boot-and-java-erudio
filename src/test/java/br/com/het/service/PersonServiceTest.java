package br.com.het.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.het.data.dto.PersonDTO;
import br.com.het.exception.RequiredObjectIsNullException;
import br.com.het.mocks.MockPerson;
import br.com.het.model.Person;
import br.com.het.repository.PersonRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

  MockPerson input;

  @InjectMocks
  private PersonService service;

  @Mock
  PersonRepository repository;

  @BeforeEach
  void setUp() {
    input = new MockPerson();
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindById() {

    // mock da entidade
    Person person = input.mockEntity(1);
    person.setId(1L);
    // mock do metodo findById
    when(repository.findById(1L)).thenReturn(Optional.of(person));
    var result = service.findById(1L);

    // verfica se o id e os links não são nulos
    assertNotNull(result);
    assertNotNull(result.getId());
    assertNotNull(result.getLinks());

    // verfica se os links hateoas estao batendo
    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

    // verfica se o metodo esta retornando os campos corretamente
    assertEquals("Address Test1", result.getAdress());
    assertEquals("First Name Test1", result.getFirstName());
    assertEquals("Last Name Test1", result.getLastName());
    assertEquals("Female", result.getGender());

  }

  @Test
  void testCreate() {

    // mock da entidade
    Person person = input.mockEntity(1);
    Person persisted = person;
    persisted.setId(1L);

    // mock do dto
    PersonDTO dto = input.mockDTO(1);

    // mocka o method save
    when(repository.save(person)).thenReturn(persisted);
    var result = service.create(dto);

    // verfica se o id e os links não são nulos
    assertNotNull(result);
    assertNotNull(result.getId());
    assertNotNull(result.getLinks());

    // verfica se os links hateoas estao batendo
    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

    // verfica se o metodo esta retornando os campos corretamente
    assertEquals("Address Test1", result.getAdress());
    assertEquals("First Name Test1", result.getFirstName());
    assertEquals("Last Name Test1", result.getLastName());
    assertEquals("Female", result.getGender());
  }

  // Testar lancamento de excessoes no metodo create
  @Test
  void testCreateWithNullPerson() {
    Exception exception = assertThrows(RequiredObjectIsNullException.class,
        () -> {
          service.create(null);
        });

    String expectedMessage = "It is not allowed to persist a null object!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testUpdate() {

    Person person = input.mockEntity(1);
    Person persisted = person;
    persisted.setId(1L);

    PersonDTO dto = input.mockDTO(1);

    when(repository.findById(1L)).thenReturn(Optional.of(person));
    when(repository.save(person)).thenReturn(persisted);
    var result = service.update(dto);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertNotNull(result.getLinks());

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(result.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

    assertEquals("Address Test1", result.getAdress());
    assertEquals("First Name Test1", result.getFirstName());
    assertEquals("Last Name Test1", result.getLastName());
    assertEquals("Female", result.getGender());
  }

  // Testar lancamento de excessoes no metodo update
  @Test
  void testUpdateWithNullPerson() {
    Exception exception = assertThrows(RequiredObjectIsNullException.class,
        () -> {
          service.update(null);
        });

    String expectedMessage = "It is not allowed to persist a null object!";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testDelete() {
    Person person = input.mockEntity(1);
    person.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(person));
    service.delete(1L);
    verify(repository, times(1)).findById(anyLong());
    verify(repository, times(1)).delete(any(Person.class));
    verifyNoMoreInteractions(repository);
  }

  @Test
  void testFindAll() {

    List<Person> list = input.mockEntityList();
    when(repository.findAll()).thenReturn(list);
    List<PersonDTO> people = service.findAll();

    assertNotNull(people);
    assertEquals(14, people.size());

    // -------------vericando primeiro elemento da lista-----------------
    var personOne = people.get(1);

    // verfica se o id e os links não são nulos
    assertNotNull(personOne);
    assertNotNull(personOne.getId());
    assertNotNull(personOne.getLinks());

    // verfica se os links hateoas estao batendo
    assertNotNull(personOne.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("GET")));

    assertNotNull(personOne.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(personOne.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(personOne.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(personOne.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/1")
            && link.getType().equals("DELETE")));

    // verfica se o metodo esta retornando os campos corretamente
    assertEquals("Address Test1", personOne.getAdress());
    assertEquals("First Name Test1", personOne.getFirstName());
    assertEquals("Last Name Test1", personOne.getLastName());
    assertEquals("Female", personOne.getGender());

    // --------------------vericando 4o elemento-----------------------------
    var personFour = people.get(4);

    // verfica se o id e os links não são nulos
    assertNotNull(personFour);
    assertNotNull(personFour.getId());
    assertNotNull(personFour.getLinks());

    // verfica se os links hateoas estao batendo
    assertNotNull(personFour.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/4")
            && link.getType().equals("GET")));

    assertNotNull(personFour.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(personFour.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(personFour.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(personFour.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/4")
            && link.getType().equals("DELETE")));

    // verfica se o metodo esta retornando os campos corretamente
    assertEquals("Address Test4", personFour.getAdress());
    assertEquals("First Name Test4", personFour.getFirstName());
    assertEquals("Last Name Test4", personFour.getLastName());
    assertEquals("Male", personFour.getGender());

    // --------------------vericando 8o elemento-----------------------------
    var personEight = people.get(8);

    // verfica se o id e os links não são nulos
    assertNotNull(personEight);
    assertNotNull(personEight.getId());
    assertNotNull(personEight.getLinks());

    // verfica se os links hateoas estao batendo
    assertNotNull(personEight.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("self")
            && link.getHref().endsWith("/api/person/v1/8")
            && link.getType().equals("GET")));

    assertNotNull(personEight.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("findAll")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("GET")));

    assertNotNull(personEight.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("create")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("POST")));

    assertNotNull(personEight.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("update")
            && link.getHref().endsWith("/api/person/v1")
            && link.getType().equals("PUT")));

    assertNotNull(personEight.getLinks().stream()
        .anyMatch(link -> link.getRel().value().equals("delete")
            && link.getHref().endsWith("/api/person/v1/8")
            && link.getType().equals("DELETE")));

    // verfica se o metodo esta retornando os campos corretamente
    assertEquals("Address Test8", personEight.getAdress());
    assertEquals("First Name Test8", personEight.getFirstName());
    assertEquals("Last Name Test8", personEight.getLastName());
    assertEquals("Male", personEight.getGender());

  }

}
