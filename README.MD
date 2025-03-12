# Morningstar 

API microservice for ticketing and reservation. Java, Spring Boot.

Function Tested and Security Tested

Authentication: JWT

Database: Postgres.

### Run

    mvnw clean package

to create jar

then run using docker compose (needs your env file)

    docker-compose up --build

or run with the java cli

    java -jar target/morningstar-0.0.1-SNAPSHOT.jar

ENV variables required to run are:
* DB_USER
* DB_PASSWORD
* DB_URL
* JWT_SIGNING_KEY: A 256 bit string


Then Visit: http://localhost:8080/swagger-ui.html
