package br.com.het.integrationtests.controllers.withxml.withjson;

import br.com.het.config.TestConfigs;
import br.com.het.integrationtests.dto.PersonDTO;
import br.com.het.integrationtests.testcontainers.AbstractIntegrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

        private static RequestSpecification specification;
        private static ObjectMapper objectMapper;

        private static PersonDTO person;

        @BeforeAll
        static void setUp() {
                objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                person = new PersonDTO();
        }

        @Test
        @Order(1)
        void createTest() throws JsonProcessingException {
                mockPerson();

                specification = new RequestSpecBuilder()
                                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_REACT)
                                .setBasePath("/api/person/v1")
                                .setPort(TestConfigs.SERVER_PORT)
                                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                                .build();

                var content = given(specification)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .body(person)
                                .when()
                                .post()
                                .then()
                                .statusCode(200)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .extract()
                                .body()
                                .asString();

                PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
                person = createdPerson;

                assertNotNull(createdPerson.getId());
                assertTrue(createdPerson.getId() > 0);

                assertEquals("Linus", createdPerson.getFirstName());
                assertEquals("Torvalds", createdPerson.getLastName());
                assertEquals("Helsinki - Finland", createdPerson.getAdress());
                assertEquals("Male", createdPerson.getGender());
                assertTrue(createdPerson.getEnabled());

        }

        @Test
        @Order(2)
        void updateTest() throws JsonProcessingException {
                person.setLastName("Benedict Torvalds");

                var content = given(specification)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .body(person)
                                .when()
                                .put()
                                .then()
                                .statusCode(200)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .extract()
                                .body()
                                .asString();

                PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
                person = createdPerson;

                assertNotNull(createdPerson.getId());
                assertTrue(createdPerson.getId() > 0);

                assertEquals("Linus", createdPerson.getFirstName());
                assertEquals("Benedict Torvalds", createdPerson.getLastName());
                assertEquals("Helsinki - Finland", createdPerson.getAdress());
                assertEquals("Male", createdPerson.getGender());
                assertTrue(createdPerson.getEnabled());

        }

        @Test
        @Order(3)
        void findByIdTest() throws JsonProcessingException {

                var content = given(specification)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .pathParam("id", person.getId())
                                .when()
                                .get("{id}")
                                .then()
                                .statusCode(200)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .extract()
                                .body()
                                .asString();

                PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
                person = createdPerson;

                assertNotNull(createdPerson.getId());
                assertTrue(createdPerson.getId() > 0);

                assertEquals("Linus", createdPerson.getFirstName());
                assertEquals("Benedict Torvalds", createdPerson.getLastName());
                assertEquals("Helsinki - Finland", createdPerson.getAdress());
                assertEquals("Male", createdPerson.getGender());
                assertTrue(createdPerson.getEnabled());
        }

        @Test
        @Order(4)
        void disableTest() throws JsonProcessingException {

                var content = given(specification)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .pathParam("id", person.getId())
                                .when()
                                .patch("{id}")
                                .then()
                                .statusCode(200)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .extract()
                                .body()
                                .asString();

                PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
                person = createdPerson;

                assertNotNull(createdPerson.getId());
                assertTrue(createdPerson.getId() > 0);

                assertEquals("Linus", createdPerson.getFirstName());
                assertEquals("Benedict Torvalds", createdPerson.getLastName());
                assertEquals("Helsinki - Finland", createdPerson.getAdress());
                assertEquals("Male", createdPerson.getGender());
                assertFalse(createdPerson.getEnabled());
        }

        @Test
        @Order(5)
        void deleteTest() throws JsonProcessingException {

                given(specification)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .pathParam("id", person.getId())
                                .when()
                                .delete("{id}")
                                .then()
                                .statusCode(204);

        }

        @Test
        @Order(6)
        void findAllTest() throws JsonProcessingException {

                var content = given(specification)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .when()
                                .get()
                                .then()
                                .statusCode(200)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .extract()
                                .body()
                                .asString();

                List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {
                });
                PersonDTO PersonOne = people.get(0);
                person = PersonOne;

                assertNotNull(PersonOne.getId());
                assertTrue(PersonOne.getId() > 0);

                assertEquals("Ayrton", PersonOne.getFirstName());
                assertEquals("Senna", PersonOne.getLastName());
                assertEquals("São Paulo -  Brasil", PersonOne.getAdress());
                assertEquals("Male", PersonOne.getGender());
                assertTrue(PersonOne.getEnabled());

                PersonDTO PersonFour = people.get(3);
                person = PersonFour;

                assertNotNull(PersonFour.getId());
                assertTrue(PersonFour.getId() > 0);

                assertEquals("Hideo", PersonFour.getFirstName());
                assertEquals("Kojima", PersonFour.getLastName());
                assertEquals("Setagaya - Tokyo - Japan", PersonFour.getAdress());
                assertEquals("Male", PersonFour.getGender());
                assertTrue(PersonFour.getEnabled());
        }

        private void mockPerson() {
                person.setFirstName("Linus");
                person.setLastName("Torvalds");
                person.setAdress("Helsinki - Finland");
                person.setGender("Male");
                person.setEnabled(true);
        }
}