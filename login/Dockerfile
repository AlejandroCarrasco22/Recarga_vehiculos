FROM maven:3-openjdk-11 AS MAVEN_BUILD
WORKDIR /app
COPY pom.xml .
COPY chargerpoints/pom.xml ./chargerpoints/
COPY vehicles/pom.xml ./vehicles/
COPY users/pom.xml ./users/
COPY shared-libraries/ ./shared-libraries/
COPY recharge/pom.xml ./recharge/
COPY login/ ./login/
# Build skipping tests to speed up the build
# Build only the employee module and its dependencies
RUN mvn -Dmaven.test.skip package -pl login -am

FROM openjdk:11-jdk-oracle AS RUNTIME
ARG JAR_FILE=/app/login/target/*.jar
COPY --from=MAVEN_BUILD ${JAR_FILE} app.jar
ENV PORT 8081
EXPOSE $PORT
ENTRYPOINT ["java","-jar", "/app.jar"]

