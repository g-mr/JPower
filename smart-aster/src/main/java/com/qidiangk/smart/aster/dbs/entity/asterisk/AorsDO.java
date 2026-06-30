package com.qidiangk.smart.aster.dbs.entity.asterisk;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

import java.math.BigInteger;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ASTERISK_DATASOURCE;

/**
 *  PJSIP的AOR配置
 *
 * @author mr.g
 */
@Data
@Table(value = "ps_aors", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AorsDO extends BaseEntity {

    /**
     * ID
     */
    @Id(keyType = KeyType.None)
    private String id;
    /**
     * 指定AOR的联系地址（SIP URI），通常留空由动态注册填充
     */
    private String contact;
    /**
     * 注册的默认过期时间（秒），客户端未指定时使用此值
     */
    private Integer defaultExpiration;
    /**
     * 与此AOR关联的语音邮箱邮箱号
     */
    private String mailboxes;
    /**
     * 允许同时注册的最大联系人数
     */
    private Integer maxContacts;
    /**
     * 允许客户端注册的最小过期时间（秒）
     */
    private Integer minimumExpiration;
    /**
     * 当达到max_contacts限制时，是否移除现有联系人以允许新注册
     */

    private Boolean removeExisting;
    /**
     * 发送OPTIONS请求检查联系是否可达的频率（秒）
     */
    private Integer qualifyFrequency;
    /**
     * 是否对qualify请求进行认证（需要认证信息）
     */
    private Boolean authenticateQualify;
    /**
     * 允许客户端注册的最大过期时间（秒）
     */
    private Integer maximumExpiration;
    /**
     * 用于发送到此AOR的请求的出站代理服务器
     */
    private String outboundProxy;
    /**
     * 是否在注册请求中包含和支持Path头字段（用于NAT穿透）
     */
    private String supportPath;
    /**
     * 等待qualify响应的超时时间（秒）
     */
    private Double qualifyTimeout;
    /**
     * 语音邮箱的分机号码
     */
    private String voicemailExtension;
    /**
     * 当联系人因qualify失败变为不可用时，是否自动移除
     */
    private Boolean removeUnavailable;
    /**
     * 是否只将2xx响应视为qualify成功（忽略其他成功响应如100、180等）
     */
    private String qualify2xxOnly;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}