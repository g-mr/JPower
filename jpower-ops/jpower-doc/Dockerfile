FROM openjdk:8-jre

MAINTAINER mr.g

RUN mkdir -p /jpower/doc \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/doc

EXPOSE 18000

ADD ./target/jpower-doc-exec.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]