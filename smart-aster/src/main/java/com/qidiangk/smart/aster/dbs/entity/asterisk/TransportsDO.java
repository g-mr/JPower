package com.qidiangk.smart.aster.dbs.entity.asterisk;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.support.type.YesOrNoEnumTypeHandler;

import java.math.BigInteger;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ASTERISK_DATASOURCE;

/**
 * SIP传输配置
 * 该表定义了 Asterisk PJSIP 传输层的参数，包括协议、绑定地址、TLS 设置、NAT 穿透等。
 *
 * @author mr.g
 */
@Data
@Table(value = "ps_transports", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportsDO extends BaseEntity {

    /**
     * 主键
     */
    @Id(keyType = KeyType.None)
    private String id;

    /**
     * 异步操作的最大并发数。
     * 对应字段：async_operations
     */
    private Integer asyncOperations;

    /**
     * 监听的本地地址和端口，格式为 "IP:端口"，例如 "0.0.0.0:5060"。
     * 对应字段：bind
     */
    private String bind;

    /**
     * CA 证书链文件路径，用于验证对端证书。
     * 对应字段：ca_list_file
     */
    private String caListFile;

    /**
     * 服务器证书文件路径（PEM 格式）。
     * 对应字段：cert_file
     */
    private String certFile;

    /**
     * 加密套件列表，用于 TLS 连接。
     * 对应字段：cipher
     */
    private String cipher;

    /**
     * TLS 服务器的域名，用于 SNI（Server Name Indication）。
     * 对应字段：domain
     */
    private String domain;

    /**
     * 外部媒体地址，用于 NAT 穿透时向外部通告的媒体 IP 或域名。
     * 对应字段：external_media_address
     */
    private String externalMediaAddress;

    /**
     * 外部信令地址，用于 NAT 穿透时向外部通告的信令 IP 或域名。
     * 对应字段：external_signaling_address
     */
    private String externalSignalingAddress;

    /**
     * 外部信令端口，如果与绑定端口不同可单独指定。
     * 对应字段：external_signaling_port
     */
    private Integer externalSignalingPort;

    /**
     * 强制使用的 TLS 协议版本。
     * 允许的值：'default', 'unspecified', 'tlsv1', 'tlsv1_1', 'tlsv1_2', 'tlsv1_3', 'sslv2', 'sslv23', 'sslv3'。
     * 对应字段：method
     */
    private String method;

    /**
     * 本地网络子网，CIDR 格式，用于判断地址是否为内网，例如 "192.168.0.0/16"。
     * 对应字段：local_net
     */
    private String localNet;

    /**
     * 私钥密码（如果私钥文件加密）。
     * 对应字段：password
     */
    private String password;

    /**
     * 私钥文件路径。
     * 对应字段：priv_key_file
     */
    private String privKeyFile;

    /**
     * 传输协议类型。
     * 允许的值：'udp', 'tcp', 'tls', 'ws', 'wss', 'flow'。
     * 对应字段：protocol
     */
    private String protocol;

    /**
     * 是否要求客户端提供证书（双向认证）。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：require_client_cert
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean requireClientCert;

    /**
     * 是否验证客户端证书。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：verify_client
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean verifyClient;

    /**
     * 是否验证服务器证书（当 Asterisk 作为客户端时使用）。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：verify_server
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean verifyServer;

    /**
     * IP 头中的服务类型（ToS）值，用于 QoS 标记，例如 "ef"。
     * 对应字段：tos
     */
    private String tos;

    /**
     * 二层 802.1p 优先级（CoS）值。
     * 对应字段：cos
     */
    private Integer cos;

    /**
     * 是否允许在运行时重载传输配置。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：allow_reload
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean allowReload;

    /**
     * 是否强制使用同一套接字收发消息，有助于对称 NAT 穿透。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：symmetric_transport
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean symmetricTransport;

    /**
     * 是否允许证书中的通配符域名（例如 *.example.com）。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     * 对应字段：allow_wildcard_certs
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean allowWildcardCerts;

    /**
     * 是否启用 TCP Keepalive。
     * 对应字段：tcp_keepalive_enable（数据库类型 tinyint(1)）
     */
    private Boolean tcpKeepaliveEnable;

    /**
     * TCP Keepalive 空闲时间（秒），连接空闲多久后开始发送探测包。
     * 对应字段：tcp_keepalive_idle_time
     */
    private Integer tcpKeepaliveIdleTime;

    /**
     * TCP Keepalive 探测间隔时间（秒）。
     * 对应字段：tcp_keepalive_interval_time
     */
    private Integer tcpKeepaliveIntervalTime;

    /**
     * TCP Keepalive 最大探测次数，超过后认为连接断开。
     * 对应字段：tcp_keepalive_probe_count
     */
    private Integer tcpKeepaliveProbeCount;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}
