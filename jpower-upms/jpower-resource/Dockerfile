FROM openjdk:8-jre

MAINTAINER mr.g

RUN mkdir -p /jpower/resource \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/resource

ENV SW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800 \
    SW_AGENT_NAME=jpower-resource

EXPOSE 84

ADD ./target/jpower-resource-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]