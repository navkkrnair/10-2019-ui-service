FROM alpine/git as clone
ENV APPROOT="/app"
WORKDIR $APPROOT
RUN git clone https://github.com/navkkrnair/10-2019-ui-service.git

FROM maven:3.6-jdk-8-alpine as build
ENV APPROOT="/app"
WORKDIR $APPROOT
COPY --from=clone $APPROOT/10-2019-ui-service $APPROOT
RUN mvn clean package -DskipTests


FROM openjdk:8-jre-alpine
MAINTAINER "navkkrnair@gmail.com"
ENV APPROOT="/app"
WORKDIR $APPROOT 
COPY --from=build $APPROOT/target/ui-service-1.0.war $APPROOT
EXPOSE 8010
ENTRYPOINT ["java"]
CMD ["-jar","-Xmx512m","-Xms512m","-Djava.security.egd=file:/dev/./urandom", "ui-service-1.0.war"]
