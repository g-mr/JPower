FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:17-anolis

MAINTAINER mr.g

RUN mkdir -p /jpower/boot \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR /jpower/boot

EXPOSE 8000

ADD ./target/jpower-boot-exec.jar ./app.jar

ENTRYPOINT ["java", "-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]