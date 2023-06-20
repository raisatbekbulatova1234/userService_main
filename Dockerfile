#Build stage

FROM gradle:latest AS BUILD
WORKDIR /UserServiceMain
COPY . .
RUN gradle build

# Package stage
FROM openjdk:19
ENV JAR_NAME=UserServiceMain.jar
ENV APP_HOME=/UserServiceMain/
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8700
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME