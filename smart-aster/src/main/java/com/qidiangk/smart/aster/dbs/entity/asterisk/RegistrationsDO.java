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
 * @author mr.g
 */
@Table(value = "ps_registrations", dataSource = ASTERISK_DATASOURCE)
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationsDO extends BaseEntity {

    /**
     *
     */
    @Id(keyType = KeyType.None)
    private String id;
    /**
     *
     */
    private String authRejectionPermanent;
    /**
     *
     */
    private String clientUri;
    /**
     *
     */
    private String contactUser;
    /**
     *
     */
    private Integer expiration;
    /**
     *
     */
    private Integer maxRetries;
    /**
     *
     */
    private String outboundAuth;
    /**
     *
     */
    private String outboundProxy;
    /**
     *
     */
    private Integer retryInterval;
    /**
     *
     */
    private Integer forbiddenRetryInterval;
    /**
     *
     */
    private String serverUri;
    /**
     *
     */
    private String transport;
    /**
     *
     */
    private String supportPath;
    /**
     *
     */
    private Integer fatalRetryInterval;
    /**
     *
     */
    private String line;
    /**
     *
     */
    private String endpoint;
    /**
     *
     */
    private String supportOutbound;
    /**
     *
     */
    private String contactHeaderParams;
    /**
     *
     */
    private Integer maxRandomInitialDelay;
    /**
     *
     */
    private String securityNegotiation;
    /**
     *
     */
    private String securityMechanisms;
    /**
     *
     */
    private String userAgent;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}