FROM openjdk:8
ADD target/code-challenge.jar code-challenge.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "code-challenge.jar"]