FROM openjdk:8-jre-slim

MAINTAINER ding931226@yeah.net

RUN mkdir -p /jpower/sentinel \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/sentinel

ENV AUTH_USERNAME=sentinel \
    AUTH_PASSWORD=sentinel

EXPOSE 8858

EXPOSE 8719

ADD ./sentinel-dashboard-1.8.3.jar ./app.jar

CMD java -Dsentinel.dashboard.auth.username=${AUTH_USERNAME} -Dsentinel.dashboard.auth.password=${AUTH_PASSWORD} -Djava.security.egd=file:/dev/./urandom -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard -jar app.jar