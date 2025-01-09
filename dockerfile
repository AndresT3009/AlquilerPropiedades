FROM openjdk:17-jdk-alpine
COPY target/AlquilerPropiedades-0.0.1-SNAPSHOT.jar alquiler.jar
ENTRYPOINT ["java","-jar","alquiler.jar"]