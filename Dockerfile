FROM eclipse-temurin:17-jdk-alpine AS builder
#Working Directory in container
WORKDIR /build
#Location of .jar file to be extracted
ARG JAR_FILE=target/morningstar-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} morningstar.jar
RUN java -Djarmode=layertools -jar morningstar.jar extract

FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]