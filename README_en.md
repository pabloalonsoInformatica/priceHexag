
# Prices API

This project is a price API developed with Spring Boot 3, designed to efficiently manage and provide information about product prices.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Code Review](#code-review)
- [Installation](#installation)
- [Execution](#execution)
- [Endpoints](#endpoints)
- [Request Example](#request-example)
- [Future Enhancements](#future-enhancements)
- [License](#license)
- [Other Information](#other-information)

## Features

- RESTful API for querying product prices.
- Allows querying prices based on store identifier, product identifier, and date.
- Adheres to SOLID and Clean Code principles.
- H2 in-memory database with automated creation and population. Includes audit fields.
- Observability: logging with traceability through a request ID.
- Error control and input parameter validation in requests.
- Automated code inspection via SonarQube (Docker container) and testing.
- Code coverage with JaCoCo and connection to SonarQube.
- API documentation with OpenAPI.
- Hexagonal Architecture with DDD.

## Technologies Used

- **Spring Boot 3**
- **Java 17**
- **Maven**
- **Spring Boot Web** for MVC implementation.
- **JPA** for database management.
- **H2** for in-memory persistence.
- **Docker** for containerizing SonarQube (optional).
- **SonarQube** for static repository analysis and connection via sonarsource-scanner.
- **JaCoCo** for code coverage analysis.
- **OpenAPI** for documentation.
- **Others:** DevTools, Lombok, Jakarta Validation (parameter validation), Starter-Test, Rest-Assured, Logback, and SLF4J.

## Architecture

Hexagonal architecture with DDD is employed, using inversion of control and dependency injection to separate layers. Database queries use the Repository and Specification pattern, allowing semantic queries for cleaner, modular, and reusable code. This pattern is also applied in the application service, making it reusable in future modules.

Layers are ***strictly isolated*** according to architecture principles. Within the infrastructure layer, models or DTOs use Lombok and JPA to establish relationships with the database.

Tests are divided into acceptance tests and unit tests. In the latter, the Mother pattern is implemented.

## Code Review

The bounded context name assigned is platform. Inside this, there is the prices module. The folder structure intentionally divides apps and contexts, enabling easy separation even in app deployment from the context.

**apps**

In apps (infrastructure), applications that access our contexts are located. The application is called "Backend," and the folders within `/src/com/inditex/apps/platform/backend` are clearly labeled: controllers, doc, dtos, middlewares, resources.

**context**

Within contexts, we have the bounded context (platform) and the prices module. Both the context and the bounded context have a shared directory (shared) for common elements of each level.

The prices module contains the application, domain, and infrastructure folders. In application, there are features like the search with the specification pattern; in domain, we have interfaces, aggregateRoots, and valueObjects; and in infrastructure, the repository pattern is used to access the database.

Testing follows the same folder structure as the source code, with acceptance tests in apps and unit tests in context.

### Requests

Below is a ***general and simplified*** description of how requests are communicated and processed through the application. Composition, inheritance, and other details are not covered:

Requests reach the PricesHttpRestController in infrastructure, delegating to the appropriate handler (PricesHttpRestGetHandler) to validate parameters and use the application layer for price retrieval.

Within application in the price module, the application service creates the domain-level query that will reach the infrastructure (repository passed in the constructor).

```java
    public PricesBySpecificationSearch(Repository<Price> repository) {
        this.repository = repository;
        this.serviceSpecificationSearch = new ServiceSpecificationSearchImpl<>(this.repository);
    }

    public PricesByServiceSpecificationSearchResponse run (PricesBySpecificationSearchQuery query){
        ServiceSpecificationSearchResponse<Price> response = this.serviceSpecificationSearch.run(Price.class, query);
        return new PricesByServiceSpecificationSearchResponse(response.getData(), response.getCount());
    }
```


```java
        //extract filters
        if (query.filters != null) {
            filters = query.filters.stream()
                    .map(this::convertToSpecificationFilter)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        Specification specification = new Specification.Builder().filters(filters).build();

        // query
        Optional<T> result = this.repository.matchingOneDocument(specification);

        //compose response
        T[] data = (T[]) Array.newInstance(type, result.isPresent() ? 1 : 0);
        result.ifPresent(value -> data[0] = value);
        Long count = result.isPresent() ? 1L : 0L;
        logger.debug("run. repository matchingOneDocument result_count: {}", count);
        return new ServiceSpecificationSearchResponse<>(data, count);
```

In the infrastructure layer, in the repository, the database request is constructed from the Specification of our domain. In this case, JPA Specification is used. The response is composed and returned between layers until it reaches the controller, where it is returned.


### Other Parts

- **HandlerExceptionController**: Captures exceptions that may occur to log them and hide them so that they are not returned as "raw" responses. The error response is modeled in `AppError`.

- **RequestIdFilter**: This filter generates and adds a request identifier, allowing each request to be tracked throughout its lifecycle. This is especially useful in request concurrency scenarios.

- **Logging**: Configured in `logback-spring.xml`, using default logging levels, and output is formatted to be sent to both standard output and a file.

- **DB Models**: Persistence entities (PriceDocument, AuditDocument) are modeled with JPA, but database seeding and schema are handled through SQL scripts (`schema.sql`, `data.sql`). SQL audit fields are added (see `AuditDocument`). For simplification, the time format used is ISO-8601, and UTC is not used as a time standard. The currency reference format is ISO 4217. Access to the database is available during execution via the web UI `http://localhost:8080/h2-console/`.

- **DTOs**: Following the single responsibility principle, different DTOs are separated by function and layer.

- **Configurations**: Default and test configurations are set in `application.yml` and `application-test.yml`.

- **Testing**: Tests are added with a test profile and the indicated technologies. They are covered with minimal and critical unit tests of the application. In the acceptance test (PlatformApplicationTests), the cases indicated in the statement are covered, as well as an additional out-of-date-range case.

## Installation

1. Install Java 17 and Maven (mvn) if they are not installed on your system.
2. Clone the repository:

## Execution

You can run the application through three options:

- Run with Maven:
    ```bash
        mvn spring-boot:run
    ```

- Run the JAR (if generated with mvn package, it will be in target/*.jar):
 ```bash
     java -jar target/prices-0.0.1-SNAPSHOT.jar
 ```

- Load the application in an IDE and run PricesApplication.

To send requests to the application, load the documentation file `prices/src/com/inditex/apps/platform/backend/doc/openapi.json` into a tool like Postman and send the requests you want to test, or send your requests from a terminal, for example:

```bash
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T10:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T16:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T21:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-15T10:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-16T21:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2021-08-16T21:00:00"
```

### tests

If you only want to run tests, you can use your IDE or run:
 ```bash
     mvn test -Dspring.profiles.active=test
 ```

### sonarQube
To use SonarQube, you must run the docker-compose.yaml file:
 ```bash
     docker-compose up -d
 ```
Then, open your web browser and go to `http://localhost:9000/`. The default username and password are admin, admin. It will then prompt you to change the password.

Once inside, go to the user account and generate an access token. Within your account in security/generate token, generate a user-type token. Copy it as you will use it later.

You can then use SonarQube from the web to link it with a repository and/or run the project for an analysis:

 ```bash
     mvn clean verify -X sonar:sonar -Dsonar.projectKey=prices_key -Dsonar.host.url=http://localhost:9000 -Dsonar.token=YOUR_TOKEN_HERE
 ```

## Endpoints
The API documentation is generated with OpenAPI. The file is located at `prices/src/com/inditex/apps/platform/backend/doc/openapi.json`. Below is a brief description of this documentation.
### Get Product Price

- **URL**: `/api/prices`
- **Method**: `GET`
- **Description**: Gets the price of a product for a brand on a specific date and time.

### Query Parameters

- **productId**:
  - **Type**: `integer`
  - **Required**: `true`
  - **Description**: Product ID (e.g., `35455`).

- **brandId**:
  - **Type**: `integer`
  - **Required**: `true`
  - **Description**: Brand ID (e.g., `1` for ZARA).

- **priceDate**:
  - **Type**: `string`
  - **Required**: `true`
  - **Description**: Date and time to apply the price in ISO-8601 format (e.g., `2020-06-14T16:00:00`).

### Responses

- **200 OK**: Price found.
  - **Example Response**:
    ```json
          {
              "prices":[
                  {
                      "priceList":1,
                      "productId":35455,
                      "brandId":1,
                      "startDate":"2020-06-14T00:00:00",
                      "endDate":"2020-12-31T23:59:59",
                      "value":35.50
                  }
              ],
              "count": 1
          }
    ```

- **404 Not Found**: No price was found for the given request.

### Request Example

Here are examples of requests you can make to the API:

1. **Price Query (Case 1)**:
   ```bash
   curl -X GET "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T16:00:00"
   ```

## Future Enhancements

- API versioning using an interceptor and passing it as a value in the request:
    ```
        @RequestHeader(value = "X-API-VERSION", required = false, defaultValue = "1") String apiVersion
    ```
- Do not hide exceptions in responses in development environments or those requiring it.
- Implementation of CI/CD.
- Definition of environments and possible additional configurations.
- Incorporation of UTC for time synchronization and coordination.
- Change acceptance (API) tests to Gherkin language, for example with cucumber.
- Improve test cases and code coverage.

## License

This project is protected by copyright. All rights reserved. Copying, distribution, or modification of this project, in whole or in part, without the express permission of the author is not allowed.

© [2024] [Pablo Alonso López]. All rights reserved.

## Other Information

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.4/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#using.devtools)
* [h2](http://www.h2database.com/html/quickstart.html)
* [Docker](https://www.docker.com/)
* [Sonarqube](https://www.sonarsource.com/products/sonarqube/)