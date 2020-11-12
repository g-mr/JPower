FROM openjdk:8-jre

MAINTAINER ding

RUN mkdir -p /jpower/admin \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/admin

EXPOSE 8868

ADD ./target/jpower-admin-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]