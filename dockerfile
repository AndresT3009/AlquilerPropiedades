FROM openjdk:21-jdk
COPY target/AlquilerPropiedades-0.0.1-SNAPSHOT.jar alquiler.jar
ENTRYPOINT ["java","-jar","alquiler.jar"]