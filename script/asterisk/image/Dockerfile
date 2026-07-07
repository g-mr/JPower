FROM andrius/asterisk:22

ENV DEBIAN_FRONTEND=noninteractive

# 安装依赖
RUN apt-get update && \
    apt-get install -y odbc-mariadb && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 将自定义配置文件复制到 /etc/asterisk/，覆盖基础镜像中的默认配置
COPY conf/ /etc/asterisk/

# 启动脚本：根据环境变量生成 /etc/odbc.ini 并动态替换配置中的占位符
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 5060/udp 5060/tcp 5038/tcp 10000-20000/udp

ENTRYPOINT ["/entrypoint.sh"]

CMD ["/usr/sbin/asterisk", "-vvvdddf", "-T", "-W", "-p"]