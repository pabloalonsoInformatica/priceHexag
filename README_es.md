# Prices API

Este proyecto es una API de precios desarrollada en Spring Boot 3, diseñada para gestionar y proporcionar información
sobre precios de productos de manera eficiente.

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Arquitectura](#arquitectura)
- [Revisión del Código](#revisión-del-código)
- [Instalación](#instalación)
- [Ejecución](#ejecución)
- [Endpoints](#endpoints)
- [Ejemplo de Solicitud](#ejemplo-de-solicitud)
- [Futuras Mejoras](#futuras-mejoras)
- [Licencia](#licencia)
- [Otra Información](#otra-información)

## Características

- API RESTful para consultar precios de productos.
- Permite consultar precios según identificador de cadena, identificador de producto y fecha.
- Sigue principios SOLID y Clean Code.
- Base de datos en memoria H2 con creación y poblado automatizados. Incluye campos de auditoría.
- Observabilidad: logging con trazabilidad mediante un identificador de solicitud (request ID).
- Control de errores y validación de parámetros de entrada en la solicitud.
- Inspección de código automatizada a través de SonarQube (contenedor Docker) y pruebas.
- Cobertura de código con JaCoCo y conexión con SonarQube.
- Documentación de la API con OpenAPI.
- Arquitectura Hexagonal con DDD.

## Tecnologías Utilizadas

- **Spring Boot 3**
- **Java 17**
- **Maven**
- **Spring Boot Web** para implementar MVC.
- **JPA** para la gestión de bases de datos.
- **H2** para persistencia en memoria.
- **Docker** para contenedorización de SonarQube (opcional).
- **SonarQube** para análisis estático del repositorio y conexión mediante sonarsource-scanner.
- **JaCoCo** para análisis de cobertura de código. 
- **OpenAPI** para documentación.
- **Otras:** DevTools, Lombok, Jakarta Validation (validación de parámetros), Starter-Test, Rest-Assured, Logback y SLF4J.

## Arquitectura

Se emplea una arquitectura hexagonal con DDD, usando inversión de control e inyección de dependencias para separar capas.
Las consultas a la base de datos usan el patrón Repository y Specification, permitiendo consultas semánticas que facilitan 
limpieza, modularidad y futuras reutilizaciones. Este patrón también se aplica en el servicio de aplicación, haciéndolo 
reutilizable en módulos futuros.

Las capas están ***estrictamente aisladas*** según los principios de la arquitectura. Dentro de la capa de infraestructura, 
los modelos o DTOs usan Lombok y JPA para establecer relaciones con la base de datos.

Las pruebas se dividen en pruebas de aceptación y pruebas unitarias. En estas últimas se ha implementado el patrón Mother.

## Revisión del Código

El nombre asigando para el bounded context es platform. Dentro de este está el módulo prices.
La estructura de carpetas divide intencionalmente en apps y contexts. Esto permitiría implementar facilmente una separación incluso en despliegue de la apps del context. 

**apps**

En apps (infraestructura), están las aplicaciones que acceden a nuestros contexts. La aplicación se llama "Backend" y 
las carpetas dentro de `/src/com/inditex/apps/platform/backend` están claramente etiquetadas: controllers, doc, dtos, 
middlewares, resources.

**context**

Dentro de contexts tenemos el bounded context (platform) y el módulo prices. Tanto en el contexto como en el bounded context 
hay un directorio compartido (shared) para elementos comunes de cada nivel. 

El módulo prices contiene las carpetas application, domain e infrastructure. En application están las features, como la 
búsqueda con el patrón specification; en domain tenemos interfaces, aggregateRoots y valueObjects; en infrastructure 
se accede a la base de datos mediante el patrón repository.

El testing sigue la misma estructura de carpetas que el código fuente, con pruebas de aceptación en apps y pruebas unitarias en context.

### Peticiones
    
A continuación, se muestra una ***descripción general y simplificada***  de cómo se comunican y procesan las peticiones a través de la
aplicación. No se entra en el detalle de composiciónes, herencias u otros:

Las peticiones llegan al PricesHttpRestController en infraestructura, delegándose al handler adecuado (PricesHttpRestGetHandler)
para validar parámetros y usar la capa de aplicación, donde se realiza la búsqueda de precios.

Dentro de application en el módulo price, se usa el servicio de aplicación para crear la consulta a nivel de dominio 
que llegará a la infraestructura (repositorio que se pasa al contructor).

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

Dentro del servicio Specification, a partir de los datos de entrada, se crea una entidad de dominio Specification, 
con la cual se invoca al repositorio. Posteriormente, la respuesta se devuelve al puerto primario. 

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

Dentro de la capa de infraestructura, en el repositorio, se construye la petición a la base de datos a partir de la especificación (Specification) de nuestro dominio. En este caso, se utiliza el Specification de JPA. La respuesta se compone y retorna entre capas hasta llegar al controlador, donde es devuelta.


### Otras Partes

- **HandlerExceptionController**: Se encarga de capturar las excepciones que se puedan producir con el fin de registrarlas en el log y ocultarlas para que no sean devueltas en “bruto” como respuesta. La respuesta de error se modela en `AppError`.

- **RequestIdFilter**: Este filtro genera y añade un identificador de solicitud, permitiendo que cada solicitud sea identificada a lo largo de su ciclo de vida. Es especialmente útil en situaciones de concurrencia de solicitudes.

- **Logging**: La configuración se establece en `logback-spring.xml`, utilizando los niveles de log por defecto, y la salida está formateada para ser enviada tanto a la salida estándar como a un archivo.

- **Modelos DB**: Las entidades de persistencia (PriceDocument, AuditDocument) se modelan con JPA, pero la siembra y el esquema de la base de datos se manejan a través de scripts SQL (`schema.sql`, `data.sql`). Se añaden campos de auditoría SQL (ver `AuditDocument`).Por simplificación el formato de tiempo usado es ISO-8601, no se está utilizando un estándar de tiempo como UTC. Como formato de referencia para la moneda, se emplea ISO 4217. Es posible acceder a la base de datos durante la ejecución a través de la ui-web `http://localhost:8080/h2-console/`.

- **DTOs**: Siguiendo el principio de responsabilidad única, se separan los distintos DTOs según su función y capa.

- **Configuraciones**: Se establecen una configuración por defecto y otra para pruebas (`application.yml` y `application-test.yml`).

- **Testing**: Las pruebas se realizan añadiendo un perfil de prueba y con las tecnologías indicadas. Se cubren con pruebas unitarias mínimas y críticas de la aplicación. En la prueba de aceptación (PlatformApplicationTests) se cubren los casos indicados en el enunciado, además de un caso adicional fuera del rango de fechas.



## Instalación


1. Instala Java 17 y Maven (mvn) si no los tienes instalados en tu sistema.
2. Clona el repositorio:
   ```bash
   git clone https://github.com/tu_usuario/tu_repositorio.git
   cd tu_repositorio
   ```
3. Revisa que la configuración sea correcta para ejecutarse en tu equipo: application.yml, application-test.yml, sonar-project.properties (opcional). Por ejemplo, verifica los puertos utilizados.
4. Ejecuta el siguiente comando desde la raíz del proyecto para descargar todas las dependencias especificadas en el archivo pom.xml:
    ```bash
    mvn clean install
    ```

## Ejecución

Puedes ejecutar la aplicación mediante tres opciones:

- Ejecutar con Maven:
    ```bash
        mvn spring-boot:run
    ```

- Ejecutar el JAR (si lo has generado con mvn package, estará en target/*.jar):
 ```bash
     java -jar target/prices-0.0.1-SNAPSHOT.jar
 ```

- Cargar la aplicación en un IDE y ejecutar PricesApplication.

Para lanzar las solicitudes contra la aplicación, carga el archivo de documentación `prices/src/com/inditex/apps/platform/backend/doc/openapi.json` en una herramienta como Postman y lanza las peticiones que quieras probar, o bien envía tus solicitudes desde una terminal, por ejemplo:

```bash
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T10:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T16:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T21:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-15T10:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-16T21:00:00"
    curl "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2021-08-16T21:00:00"
```


### tests

si solo quieres lanzar los test entonces puedes usar tu IDE o ejecutar:
 ```bash
     mvn test -Dspring.profiles.active=test
 ```

### sonarQube
para usar sonarQube debes ejecutar el archivo docker-compose.yaml
 ```bash
     docker-compose up -d
 ```
despues accede a tu navegador web e ingresa en la ruta `http://localhost:9000/`. La contraseña y usuario por defecto es admin, admin. Seguido te pedirá cambiar la contraseña.

Una vez dentro debes ir a la cuenta de usuario y generar un token de acceso. Dentro de tu cuenta en security/generate token, genera de tipo user. Copialo porque lo usaras posteriorme.

despues pudes usar sonarqube desde la web para vincularlarlo con un repositorio y/o ejecutar el proyecto para realizar un análisis:

 ```bash
     mvn clean verify -X sonar:sonar -Dsonar.projectKey=prices_key -Dsonar.host.url=http://localhost:9000 -Dsonar.token=YOUR_TOKEN_HERE
 ```

## Endpoints
La documentación de la API se ha realizado con OpenAPI. El archivo se ubica en `prices/src/com/inditex/apps/platform/backend/doc/openapi.json`. A continuación, se presenta una breve descripción de dicha documentación.
### Obtener Precio de Producto

- **URL**: `/api/prices`
- **Método**: `GET`
- **Descripción**: Obtiene el precio de un producto para una marca en una fecha y hora específica.

### Parámetros de Consulta

- **productId**:
    - **Tipo**: `integer`
    - **Requerido**: `true`
    - **Descripción**: ID del producto (ej. `35455`).

- **brandId**:
    - **Tipo**: `integer`
    - **Requerido**: `true`
    - **Descripción**: ID de la marca (ej. `1` para ZARA).

- **priceDate**:
    - **Tipo**: `string`
    - **Requerido**: `true`
    - **Descripción**: Fecha y hora para aplicar el precio en formato ISO-8601 (ej. `2020-06-14T16:00:00`).

### Respuestas

- **200 OK**: Precio encontrado.
    - **Ejemplo de Respuesta**:
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

- **404 Not Found**: No se encontró precio para la solicitud dada.

### Ejemplo de Solicitud

Aquí hay ejemplos de solicitudes que puedes hacer a la API:

1. **Consulta de Precio (Caso 1)**:
   ```bash
   curl -X GET "http://localhost:8081/api/prices?productId=35455&brandId=1&priceDate=2020-06-14T16:00:00"


## Futuras Mejoras

- Versionado de la API utilizando un interceptor y pasándolo como valor en la solicitud:
    ```
        @RequestHeader(value = "X-API-VERSION", required = false, defaultValue = "1") String apiVersion
    ```
- No ocultar las excepciones en las respuestas en entornos de desarrollo o en aquellos donde se requiera.
- Implementación de CI/CD.
- Definición de entornos y posibles configuraciones adicionales.
- Incorporación de UTC para la sincronización y coordinación de tiempo.
- Cambiar los test de aceptación (api) a lenguaje Gherkin, por ejemplo con cucumber.
- Mejora de casos de prueba y cobertura de código.

## Licencia

Este proyecto está protegido por derechos de autor. Todos los derechos reservados. No se permite la copia, distribución o modificación de este proyecto, en su totalidad o en parte, sin el permiso expreso del autor.

© [2024] [Pablo Alonso López]. Todos los derechos reservados.

## Otra Información

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.3.4/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.3.4/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#web)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.3.4/reference/htmlsingle/index.html#using.devtools)
* [h2](http://www.h2database.com/html/quickstart.html)
* [Docker](https://www.docker.com/)
* [Sonarqube](https://www.sonarsource.com/products/sonarqube/)