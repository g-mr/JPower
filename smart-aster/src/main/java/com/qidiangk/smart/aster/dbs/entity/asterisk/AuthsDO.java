package com.qidiangk.smart.aster.dbs.entity.asterisk;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.*;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import com.qidiangk.smart.aster.constants.EndpointsAuthTypeEnum;

import java.math.BigInteger;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ASTERISK_DATASOURCE;

/**
 *  PJSIP的AUTH配置
 *
 * @author mr.g
 */
@Data
@Builder
@Table(value = "ps_auths", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthsDO extends BaseEntity {

    /**
     * ID
     */
    @Id(keyType = KeyType.None)
    private String id;
    /**
     * 授权类型
     */
    private EndpointsAuthTypeEnum authType;
    /**
     *
     */
    private Integer nonceLifetime;
    /**
     *
     */
    private String md5Cred;
    /**
     * 密码
     */
    private String password;
    /**
     *
     */
    private String realm;
    /**
     * 账号
     */
    private String username;
    /**
     *
     */
    private String refreshToken;
    /**
     *
     */
    private String oauthClientid;
    /**
     *
     */
    private String oauthSecret;
    /**
     *
     */
    private String passwordDigest;
    /**
     *
     */
    private String supportedAlgorithmsUas;
    /**
     *
     */
    private String supportedAlgorithmsUac;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}