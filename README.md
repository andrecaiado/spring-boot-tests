# Spring Boot tests project
This is a Spring Boot applications with slice tests and integration tests.

The tests implemented in this project are the following:

- Slice tests
  - [Repository layer](#slice-tests---repository-layer)
  - [Service layer](#slice-tests---service-layer)
  - [Web layer](#slice-tests---web-layer)
- Integration tests
  - With an embedded servlet container
  - Mocking the servlet container

## Slice tests

WHAT ARE SLICE TESTS

### Repository layer

The repository slice tests are implemented using `@DataJpaTest` annotation. This annotation will bootstrap only the repository layer of the application and will not load the full application context. It will also autoconfigure `TestEntityManager` which provides a subset of `EntityManager` methods.

When using `@DataJpaTest` annotation, an in-memory database is autoconfigured and replaces any explicit or autoconfigured DataSource.
We will use the `@AutoConfigureTestDatabase` annotation to override this default behavior and use the database configuration we provide for tests in the `src/test/resources/application.yaml` file that will bootstrap an H2 database.

```
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTest {
  ...
}
```

The H2 database will be populated by Flyway with the schema and data from the `src/main/resources/db/migration` folder. The tests will be executed upon this database and data.

**Notes:**
- By default, all tests decorated with the `@DataJpaTest` annotation become transactional. This means that the test will run within a transaction that will be rolled back at the end of the test.

### Service layer

Slice tests usually refers to the repository or web layer, nevertheless, we are going to use the term to refer to the service layer tests as well.

We use the SpringExtension class provided by the Spring Framework to enable integration between Spring and JUnit. To do this, we use the `@ExtendWith` annotation and pass the `Spring Extension` class as a parameter.

```
@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {
  ...
}
```

### Web layer

The web layer slice tests are implemented using `@WebMvcTest` annotation. This annotation will bootstrap only the web layer of the application and will not load the full application context. It will also autoconfigure `MockMvc` which provides a way to test the web layer of the application.

```
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
  ...
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

### With an embedded servlet container

This is the default behavior of the `@SpringBootTest` annotation. It will start the embedded servlet container and will bootstrap the full application context.

```
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {
  
  @Autowired
  private TestRestTemplate testRestTemplate;
  ...
}
```

In the above example, we are starting the embedded servlet container with a random port. This is useful when we have multiple tests running in parallel and we want to avoid port conflicts.

The `TestRestTemplate` will be autoconfigured to use the random port and that's why we can use relative paths instead of absolute paths.

**Notes:**

As the `testDeleteEmployeeById` deletes the record from the embedded database, the following was implemented:
- The test was annotated with `@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)` so the context is reloaded after the method is executed and the database is recreated.
- The database name was appended with a random uuid, so it forces to create a new instance of the database.

### Mocking the servlet container

This is an alternative to the previous approach. We can use the `@SpringBootTest` annotation with the `@AutoConfigureMockMvc` annotation to mock the servlet container.

```
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIT {
  ...
}
```