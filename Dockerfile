FROM registry.access.redhat.com/ubi8/openjdk-17:1.11 AS builder
#Working Directory in container
WORKDIR /build
#Location of .jar file to be extracted
ARG JAR_FILE=target/ije-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ije.jar
RUN java -Djarmode=layertools -jar ije.jar extract

FROM registry.access.redhat.com/ubi8/openjdk-17:1.11
EXPOSE 8080
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]