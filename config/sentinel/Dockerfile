FROM openjdk:8-jre-slim

MAINTAINER ding931226@yeah.net

RUN mkdir -p /jpower/sentinel \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/sentinel

EXPOSE 8858

EXPOSE 8719

ADD ./sentinel-dashboard.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8858", "-Dcsp.sentinel.dashboard.server=localhost:8858", "-Dproject.name=sentinel-dashboard", "-jar", "app.jar"]