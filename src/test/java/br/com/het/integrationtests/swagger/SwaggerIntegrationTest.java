package br.com.het.integrationtests.swagger;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.het.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.het.config.TestConfig;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

  @Test
  public void shouldDisplaySwaggerUIPage() {
    var content = given()
        .basePath("/swagger-ui/index.html")
        .port(TestConfig.SERVER_PORT)
        .when()
        .get()
        .then()
        .statusCode(200)
        .extract()
        .body()
        .asString();
    assertTrue(content.contains("Swagger UI"));
  }
}
