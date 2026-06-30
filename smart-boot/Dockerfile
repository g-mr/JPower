FROM registry.cn-hangzhou.aliyuncs.com/dockerhub_mirror/java:17-anolis

MAINTAINER mr.g

RUN mkdir -p /smart/boot \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \

WORKDIR /smart/boot

# 复制后端jar
ADD ./target/smart-boot-exec.jar ./app.jar

# 暴露端口
EXPOSE 9999

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=test"]
