# Spring Boot tests project
This is a Spring Boot applications with integration tests and slice tests.

## Tests
- Repository slice tests

### Repository slice tests

The repository slice tests are implemented using `@DataJpaTest` annotation. 

This annotation will bootstrap only the repository layer of the application and will not load the full application context. It will also autoconfigure `TestEntityManager` which provides a subset of `EntityManager` methods.

When using `@DataJpaTest` annotation, an in-memory database is autoconfigured and replaces any explicit or autoconfigured DataSource.
We will use the `@AutoConfigureTestDatabase` annotation to override this default behavior and use the database configuration we provide for tests in the `src/test/resources/application.yaml` file that will bootstrap an H2 database.

The H2 database will be populated by Flyway with the schema and data from the `src/main/resources/db/migration` folder. The tests will be executed upon this database and data.

**Notes:**
- By default, all tests decorated with the `@DataJpaTest` annotation become transactional. This means that the test will run within a transaction that will be rolled back at the end of the test.