# CITY LIST #

## Introduction ##

The application "City list" provides a list of cities. The application contains CRUD api to work with cities.
Default list of cities is filled during the application is starting.

## Getting started ##

### Prerequisites ###

* Java (JDK 17)
* Spring Boot 
* Postgres
* Maven
* Docker

### Setting up ###

1) Install dependencies  
  ``mvn install ``<br>
2) Build application<br>
   ``mvn package ``<br>
3) Create docker image<br>
   ``docker build -t city-list .``
4) Run application <br>
    ``docker-compose up ``<br>
     
Required environment variables:
* for application:
  * SPRING_DATASOURCE_URL
  * SPRING_DATASOURCE_USERNAME
  * SPRING_DATASOURCE_PASSWORD
* for database:
  * POSTGRES_DB
  * POSTGRES_USER
  * POSTGRES_PASSWORD
      
Environment variables can be managed in docker-compose.yml.      
      
Swagger will be available [here](http://localhost:8080/swagger-ui/index.html).

### Tests ###

Run test ``mvn test`` <br>
