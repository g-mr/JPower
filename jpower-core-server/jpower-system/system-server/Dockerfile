FROM openjdk:8-jre

MAINTAINER ding

RUN mkdir -p /jpower/system \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/system

ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800 \
    SW_AGENT_NAME=jpower-system

EXPOSE 81

ADD ./target/jpower-system-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]