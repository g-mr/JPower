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
 * PJSIP Endpoint 识别规则配置实体类，对应数据库表 ps_endpoint_id_ips。
 * 该表用于定义通过 IP 地址、SIP 头或请求 URI 匹配到特定 endpoint 的规则，
 *
 * @author mr.g
 */
@Data
@Builder
@Table(value = "ps_endpoint_id_ips", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EndpointIdIpDO extends BaseEntity {

    /**
     * 识别规则的唯一标识符。
     */
    @Id(keyType = KeyType.None)
    private String id;

    /**
     * 当匹配成功时，关联的 endpoint 名称。
     */
    private String endpoint;

    /**
     * 匹配的 IP 地址或 CIDR 网段，例如 "192.168.1.1" 或 "192.168.0.0/16"。
     */
    private String match;

    /**
     * 是否启用 SRV 查找，用于解析域名形式的匹配地址。
     * 数据库存储为 enum('0','1','off','on','false','true','no','yes')，经 TypeHandler 转换为 Boolean。
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean srvLookups;

    /**
     * 匹配 SIP 消息头部的字段，格式为 "header: pattern"，例如 "X-MyHeader: 123"。
     */
    private String matchHeader;

    /**
     * 匹配 SIP 请求 URI 的正则表达式或模式。
     */
    private String matchRequestUri;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}
