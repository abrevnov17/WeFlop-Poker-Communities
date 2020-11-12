FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD data/evaluator_data/TwoPlusTwo/HandRanks.dat data/evaluator_data/TwoPlusTwo/HandRanks.dat
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Xms140m", "-Xmx512m", "-jar","/app.jar"]