# Spring Boot tests project
This is a Spring Boot applications with slice tests and integration tests.

The tests implemented in this project are the following:

- [Slice tests](#slice-tests)
  - [Repository layer](#repository-layer)
  - [Service layer](#service-layer)
  - [Web layer](#web-layer)
- [Integration tests](#integration-tests)
  - [With an embedded servlet container](#with-an-embedded-servlet-container)
  - [Mocking the servlet container](#mocking-the-servlet-container)

**Notes on running the tests**:

The [Maven Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/usage.html) and [Maven Failsafe](https://maven.apache.org/surefire/maven-failsafe-plugin/index.html) plugins are configured in the [pom.xml](pom.xml) to run the slice tests (unit tests) and the integration-tests.

Run the slice tests:
```shell
mvn surefire:test
```

Run the integration tests:
```shell
mvn failsafe:integration-test
```

Run all the tests:
```shell
mvn verify
```

## Slice tests

Slice tests allows to perform isolated tests within a slice of the application, e.g., repository slice, web slice. In slice tests, dependencies need to be mocked.

### Repository slice

The repository slice tests are implemented using `@DataJpaTest` annotation. This annotation will bootstrap only the repository layer of the application and will not load the full application context. It will also autoconfigure `TestEntityManager` which provides a subset of `EntityManager` methods.

When using `@DataJpaTest` annotation, an in-memory database is autoconfigured and replaces any explicit or autoconfigured DataSource.
We will use the `@AutoConfigureTestDatabase` annotation to override this default behavior and use the database configuration we provide for tests in the `src/test/resources/application.yaml` file that will bootstrap an H2 database.

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {
  // Add tests here
}
```

The H2 database will be populated by Flyway with the schema and data from the `src/main/resources/db/migration` folder. The tests will be executed upon this database and data.

**Notes:**
- By default, all tests decorated with the `@DataJpaTest` annotation become transactional. This means that the test will run within a transaction that will be rolled back at the end of the test.

### Service slice

Slice tests usually refers to the repository or web layer, nevertheless, we are going to use the term to refer to the service layer tests as well.

We use the SpringExtension class provided by the Spring Framework to enable integration between Spring and JUnit. To do this, we use the `@ExtendWith` annotation and pass the `Spring Extension` class as a parameter.

```java
@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
    
}
```

### Web slice

The web layer slice tests are implemented using `@WebMvcTest` annotation. This annotation will bootstrap only the web layer of the application and will not load the full application context. It will also autoconfigure `MockMvc` which provides a way to test the web layer of the application.

```java
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    
}
```

The implemented tests cover the following responsibilities:
- Verifying HTTP Request Matching, response status, and response body.
- Verifying Input Deserialization and Output Serialization.
- Verifying Input Validation.
- Verifying Business Logic Calls (service).
- Verifying Exception Handling

## Integration tests

Integration tests with a servlet container are implemented using the `@SpringBootTest` annotation. This annotation will bootstrap the full application context and will start the embedded servlet container.

When using this annotation, Spring Boot automatically searches for a class annotated with `SpringBootConfiguration` up in the hierarchy. Usually, the entry point of the application is annotated with `SpringBootApplication` which is a meta-annotation for `SpringBootConfiguration`, therefore, Spring Boot will find the entry point of the application and bootstrap the full application context.

If we want to explicitly specify the classes we want to use for configuration, we can combine the `@SpringBootTest` annotation with the `@ContextConfiguration` annotation.

The `@SpringBootTest` annotation will also autoconfigure a `TestRestTemplate`so we can test the endpoints.

**Notes:**

This project follows the approach that says that unit and integration tests should have different source and resource directories. For this purpose, the following folders were created:

- src/integration-test/java
- src/integration-test/resources

The folders were created manually and the [Build Helper Maven plugin](https://www.mojohaus.org/build-helper-maven-plugin/) was configured in the [pom.xml](pom.xml) to set the directories as source and resources.

### With an embedded servlet container

This is the default behavior of the `@SpringBootTest` annotation. It will start the embedded servlet container and will bootstrap the full application context.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {
  
  @Autowired
  private TestRestTemplate testRestTemplate;

}
```

In the above example, we are starting the embedded servlet container with a random port. This is useful when we have multiple tests running in parallel and we want to avoid port conflicts.

The `TestRestTemplate` will be autoconfigured to use the random port and that's why we can use relative paths instead of absolute paths.

**Notes:**

As the `testDeleteEmployeeById` deletes the record from the embedded database, the following was implemented:
- The test was annotated with `@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)` so the context is reloaded after the method is executed and the database is recreated.
- The database name was appended with a random uuid, so it forces to create a new instance of the database.

### Mocking the servlet container

A mocked servlet container allows to process the requests through the DispatcherServlet but without running a servlet container.

Adding the `@AutoConfigureMockMvc` annotation will autoconfigure the `MockMvc` bean that we can then inject in the tests.

```java
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

  @Autowired
  private MockMvc mockMvc;

}
```
