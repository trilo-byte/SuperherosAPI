# SuperherosAPI

A small API for managing superheros as part as a challenge for mindata. It takes advantage of spring boot for rapid development. An imagen has been "dockerized" ready to give it a go.

##  🚀 Getting started

### Dependencies
- org.springframework.boot
   - spring-boot-starter-web
   - spring-boot-starter-validation
   - spring-boot-starter-actuator
   - spring-boot-starter-security
   - spring-boot-starter-data-jpa
   - spring-boot-starter-cache
   - spring-boot-starter-test
   - spring-boot-maven-plugin
- io.springfox
- org.hibernate
- hibernate-jpamodelgen
- com.h2database
- org.flywaydb
- [Mapstruct](https://mapstruct.org/): Code generator that greatly simplifies the implementation of mappings between Java bean types.
- org.openapitools

### 🔧 Installation

To run and develop over this service you must have the following requirements:
- [OpenJDK 11 (LTS) Hotspot](https://adoptopenjdk.net/installation.html#installers)
- [Maven 3.6.3](https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/). Configure your `settings.xml` following [this guide](https://developers.inditex.com/tools/distribution-platform/?target=_blank#setup-maven).
- Docker

Run `mvn clean verify` to check that everything works.

### Execution

If you want you can start the service locally with 
```
cd micsuperheros-api-rest
mvn spring-boot:run
```
Once is up you can check it on 
```
http://localhost:8083/actuator/health
```

### ⚙️ Local testing
If you want to run the test from console, just:
```
mvn test
```
Or from the IDE you would like

## 🛠 Technical design
### Architecture diagram

### Project structure
- micsuperheros-api-rest: all core logic
- micsuperheros-openapi: REST API controller's interfaces and models

### Layers

This project uses an N-Layer architecture, containing the following packages:

- /aspect: Used to implement the time consumption of service executions
- /config: Spring java-configurations
- /controllers: Implementation of REST API controllers (REST endpoints).
- /exceptions: Hierarchy of exceptions specific to the microservice.
- /mappers: Mapstruct mappers between entities and DTOs.
- /persistence: entities and repository for data presistence
- /security: secure resources by roles
- /services: Services that contain a single responsibility within the application
- /utils: Used for messages translation

### Security
The user needs to be authorized in order to use the API REST. 
For rapid development and just for demoing pursopues basic-auth is used
```
user@mock.es for read access 
admin@mock.es for read/write access
```

## ️ Author

* **Antonio Otero Andría** - **a.otero.andria@gmail.com**

### SuperherosAPI Docker repository
- https://hub.docker.com/repository/docker/porryman/microservice-docker-micsuperheros-api-rest

