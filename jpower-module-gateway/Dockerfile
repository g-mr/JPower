FROM openjdk:8-jre

MAINTAINER ding

RUN mkdir -p /jpower/gateway \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/gateway

ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800 \
    SW_AGENT_NAME=jpower-gateway

EXPOSE 80

ADD ./target/jpower-gateway-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]