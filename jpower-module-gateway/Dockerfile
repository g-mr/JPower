FROM openjdk:8-jre

MAINTAINER null

RUN mkdir -p /jpower/gateway \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/gateway

EXPOSE 80

ADD ./target/jpower-gateway-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]