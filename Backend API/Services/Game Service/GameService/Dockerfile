FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD data/evaluator_data/TwoPlusTwo/HandRanks.dat data/evaluator_data/TwoPlusTwo/HandRanks.dat
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Xms2048m", "-Xmx10240m", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]