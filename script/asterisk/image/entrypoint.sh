#!/bin/bash
set -e

# 如果 /etc/odbc.ini 不存在 或者 文件大小为0（为空），则根据环境变量生成
if [ ! -s /etc/odbc.ini ]; then
    echo "Warning: /etc/odbc.ini is missing or empty. Generating from environment variables..."
    cat > /etc/odbc.ini <<EOF
[asterisk-connector]
Description = MySQL connection to 'asterisk' database
Driver = ${DB_DRIVER:-MariaDB Unicode}
Database = ${DB_DATABASE:-asterisk}
Server = ${DB_HOST:-127.0.0.1}
Port = ${DB_PORT:-3306}
UserName = ${DB_USERNAME:-asterisk}
Password = ${DB_PASSWORD:-asterisk}
Option = ${DB_OPTION:-3}
Charset = ${DB_CHARSET:-utf8}
EOF
fi

# 动态替换 res_odbc.conf 中的占位符为环境变量的值
if [ -f /etc/asterisk/res_odbc.conf ]; then
    sed -i "s/{{DB_USERNAME}}/${DB_USERNAME:-root}/g" /etc/asterisk/res_odbc.conf
    sed -i "s/{{DB_PASSWORD}}/${DB_PASSWORD:-123456}/g" /etc/asterisk/res_odbc.conf
    echo "res_odbc.conf: username and password replaced from environment variables."
fi

# 动态配置 pjsip.conf 中的外网地址（external_media_address 和 external_signaling_address）
if [ -f /etc/asterisk/pjsip.conf ]; then
    if [ -n "$EXTERNAL_ADDRESS" ]; then
        # 环境变量已设置，替换占位行为两条实际配置
        sed -i "s/^;{{EXTERNAL_ADDRESS}}/external_media_address=${EXTERNAL_ADDRESS}\nexternal_signaling_address=${EXTERNAL_ADDRESS}/" /etc/asterisk/pjsip.conf
        echo "pjsip.conf: external_media_address and external_signaling_address set to ${EXTERNAL_ADDRESS}"
    else
        # 环境变量未设置，删除占位行（保持不配置）
        sed -i "/^;{{EXTERNAL_ADDRESS}}/d" /etc/asterisk/pjsip.conf
    fi
fi

# 动态替换 extensions.conf 中的 AGI 服务地址
if [ -f /etc/asterisk/extensions.conf ]; then
    sed -i "s/{{AGI_HOST}}/${AGI_HOST:-smartaster}/g" /etc/asterisk/extensions.conf
    echo "extensions.conf: AGI host set to ${AGI_HOST:-smartaster}"
fi

exec "$@"