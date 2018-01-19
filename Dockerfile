FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/inventory-service*.jar inventory-service.jar
RUN sh -c 'touch /inventory-service.jar'
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "inventory-service.jar" ]