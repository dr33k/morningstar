# Morningstar 

API microservice for ticketing and reservation. Java, Spring Boot.

Authentication: JWT

Database: Postgres.

### Run

    mvnw package

to create jar

then

    docker-compose up --build

Because I did not wish to hardcode the envs this time.

Visit: http://localhost:8080/swagger-ui.html
