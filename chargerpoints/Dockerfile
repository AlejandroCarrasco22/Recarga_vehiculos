FROM maven:3-openjdk-11 AS MAVEN_BUILD
RUN apt-get update && apt-get install -y default-mysql-client
WORKDIR /app
COPY pom.xml .
COPY users/pom.xml ./users/
COPY vehicles/pom.xml ./vehicles/
COPY shared-libraries/ ./shared-libraries/
COPY login/pom.xml ./login/
COPY recharge/pom.xml ./recharge/
COPY chargerpoints/ ./chargerpoints/
# Build skipping tests to speed up the build
# Build only the employee module and its dependencies
RUN mvn -Dmaven.test.skip package -pl chargerpoints -am

FROM openjdk:11-jdk AS RUNTIME
RUN apt-get update && apt-get install -y default-mysql-client
ARG JAR_FILE=/app/chargerpoints/target/*.jar
COPY --from=MAVEN_BUILD ${JAR_FILE} app.jar
ENV PORT 8080
EXPOSE $PORT
ENTRYPOINT ["java","-jar", "/app.jar"]
