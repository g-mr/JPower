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
 *  DO
 *
 * @author mr,g
 */
@Data
@Builder
@Table(value = "ps_contacts", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContactsDO extends BaseEntity {

    /**
     *
     */
    @Id(keyType = KeyType.None)
    private String id;
    /**
     *
     */
    private String uri;
    /**
     *
     */
    private Long expirationTime;
    /**
     *
     */
    private Integer qualifyFrequency;
    /**
     *
     */
    private String outboundProxy;
    /**
     *
     */
    private String path;
    /**
     *
     */
    private String userAgent;
    /**
     *
     */
    private Double qualifyTimeout;
    /**
     *
     */
    private String regServer;
    /**
     *
     */
    private String authenticateQualify;
    /**
     *
     */
    private String viaAddr;
    /**
     *
     */
    private Integer viaPort;
    /**
     *
     */
    private String callId;
    /**
     *
     */
    private String endpoint;
    /**
     *
     */
    private String pruneOnBoot;
    /**
     *
     */
    private String qualify2xxOnly;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}