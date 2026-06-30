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
import com.qidiangk.smart.aster.constants.EndpointsDtmfModeEnum;
import com.qidiangk.smart.aster.constants.EndpointsMediaEncryptionEnum;
import com.qidiangk.smart.aster.constants.EndpointsTypeEnum;

import java.math.BigInteger;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ASTERISK_DATASOURCE;

/**
 *  PJSIP的endpoints配置
 *
 * @author mr.g
 */
@Table(value = "ps_endpoints", dataSource = ASTERISK_DATASOURCE)
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointsDO extends BaseEntity {

    /**
     *
     */
    @Id(keyType = KeyType.None)
    private String id;
    /**
     * 端点类型
     *
     * @see EndpointsTypeEnum
     */
    private String businessType;
    /**
     * 传输方式
     */
    private String transport;
    /**
     * 关联Aors表
     */
    private String aors;
    /**
     * 关联Auth表
     */
    private String auth;
    /**
     *
     */
    private String context;
    /**
     * 不允许
     */
    private String disallow;
    /**
     * 允许
     */
    private String allow;
    /**
     * 是否启用直接媒体
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean directMedia;
    /**
     *
     */
    private String connectedLineMethod;
    /**
     *
     */
    private String directMediaMethod;
    /**
     *
     */
    private String directMediaGlareMitigation;
    /**
     *
     */
    private String disableDirectMediaOnNat;
    /**
     * DTMF模式
     */
    private EndpointsDtmfModeEnum dtmfMode;
    /**
     * 媒体代理
     */
    private String externalMediaAddress;
    /**
     * 是否强制返回RPORT
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean forceRport;
    /**
     * 是否使用ICE
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean iceSupport;
    /**
     * 识别方式
     *
     * ip=IP识别、username=用户名识别、auth_username=鉴权用户名识别
     */
    private String identifyBy;
    /**
     * 邮件箱
     */
    private String mailboxes;
    /**
     * 媒体建议
     */
    private String mohSuggest;
    /**
     * 出站认证
     */
    private String outboundAuth;
    /**
     * 出站代理
     */
    private String outboundProxy;
    /**
     * 是否重写Contact头
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean rewriteContact;
    /**
     * 是否使用RTP会话IPv6
     */
    private String rtpIpv6;
    /**
     * 是否使用RTP会话symmetry
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean rtpSymmetric;
    /**
     * 是否发送Diversion头
     */
    private String sendDiversion;
    /**
     * 是否发送Pai头
     */
    private String sendPai;
    /**
     * 是否发送Rpid头
     */
    private String sendRpid;
    /**
     * 最小超时时间
     */
    private Integer timersMinSe;
    /**
     * 定时器设置
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean timers;
    /**
     * 是否使用会话定时器
     */
    private Integer timersSessExpires;
    /**
     * 线路号码
     */
    private String callerid;
    /**
     * 呼叫者ID隐私
     */
    private String calleridPrivacy;
    /**
     * 呼叫者ID标签
     */
    private String calleridTag;
    /**
     * 100rel
     */
    @Column("100rel")
    private String rel;
    /**
     * 是否聚合MWI
     */
    private String aggregateMwi;
    /**
     * 入站ID验证
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean trustIdInbound;
    /**
     * 出站ID验证
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean trustIdOutbound;
    /**
     * 是否使用Ptime
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean usePtime;
    /**
     * 是否使用AVPF
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean useAvpf;
    /**
     * 音频加密模式
     */
    private EndpointsMediaEncryptionEnum mediaEncryption;
    /**
     * 内带进度
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean inbandProgress;
    /**
     * 呼叫组
     */
    private String callGroup;
    /**
     * 拨号组
     */
    private String pickupGroup;
    /**
     * 命名呼叫组
     */
    private String namedCallGroup;
    /**
     * 命名拨号组
     */
    private String namedPickupGroup;
    /**
     * 忙时设备状态
     */
    private Integer deviceStateBusyAt;
    /**
     * 传真检测
     */
    private String faxDetect;
    /**
     * T38 UDPTL
     */
    @Column("t38_udptl")
    private String t38Udptl;
    /**
     * T38 UDPTL EC
     */
    @Column("t38_udptl_ec")
    private String t38UdptlEc;
    /**
     * T38 UDPTL最大数据报文
     */
    @Column("t38_udptl_maxdatagram")
    private Integer t38UdptlMaxdatagram;
    /**
     * T38 UDPTL NAT
     */
    @Column("t38_udptl_nat")
    private String t38UdptlNat;
    /**
     * T38 UDPTL IPv6
     */
    private String t38UdptlIpv6;
    /**
     * 时区
     */
    private String toneZone;
    /**
     * 语言
     */
    private String language;
    /**
     * 一键录音
     */
    private String oneTouchRecording;
    /**
     * 录音开始特征
     */
    private String recordOnFeature;
    /**
     * 录音结束特征
     */
    private String recordOffFeature;
    /**
     * RTP引擎
     */
    private String rtpEngine;
    /**
     * 是否允许转移
     */
    private String allowTransfer;
    /**
     * 是否允许订阅
     */
    private String allowSubscribe;
    /**
     * SDP所有者
     */
    private String sdpOwner;
    /**
     * SDP会话
     */
    private String sdpSession;
    /**
     * 音频TOS
     */
    private String tosAudio;
    /**
     * 视频TOS
     */
    private String tosVideo;
    /**
     * 子最小过期时间
     */
    private Integer subMinExpiry;
    /**
     * 来自域名
     */
    private String fromDomain;
    /**
     * 来自用户
     */
    private String fromUser;
    /**
     * MWI来自用户
     */
    private String mwiFromUser;
    /**
     * DTLS验证
     */
    private String dtlsVerify;
    /**
     * DTLS重键
     */
    private String dtlsRekey;
    /**
     *
     */
    private String dtlsCertFile;
    /**
     *
     */
    private String dtlsPrivateKey;
    /**
     *
     */
    private String dtlsCipher;
    /**
     *
     */
    private String dtlsCaFile;
    /**
     *
     */
    private String dtlsCaPath;
    /**
     *
     */
    private String dtlsSetup;
    /**
     *
     */
    @Column("srtp_tag_32")
    private String srtpTag32;
    /**
     *
     */
    private String mediaAddress;
    /**
     *
     */
    private String redirectMethod;
    /**
     *
     */
    private String setVar;
    /**
     *
     */
    private Integer cosAudio;
    /**
     *
     */
    private Integer cosVideo;
    /**
     *
     */
    private String messageContext;
    /**
     *
     */
    private String forceAvp;
    /**
     *
     */
    private String mediaUseReceivedTransport;
    /**
     *
     */
    private String accountcode;
    /**
     *
     */
    private String userEqPhone;
    /**
     *
     */
    private String mohPassthrough;
    /**
     *
     */
    private String mediaEncryptionOptimistic;
    /**
     *
     */
    private String rpidImmediate;
    /**
     *
     */
    @Column("g726_non_standard")
    private String g726NonStandard;
    /**
     *
     */
    private Integer rtpKeepalive;
    /**
     *
     */
    private Integer rtpTimeout;
    /**
     *
     */
    private Integer rtpTimeoutHold;
    /**
     *
     */
    private String bindRtpToMediaAddress;
    /**
     *
     */
    private String voicemailExtension;
    /**
     *
     */
    private String mwiSubscribeReplacesUnsolicited;
    /**
     *
     */
    private String deny;
    /**
     *
     */
    private String permit;
    /**
     *
     */
    private String acl;
    /**
     *
     */
    private String contactDeny;
    /**
     *
     */
    private String contactPermit;
    /**
     *
     */
    private String contactAcl;
    /**
     *
     */
    private String subscribeContext;
    /**
     *
     */
    private Integer faxDetectTimeout;
    /**
     *
     */
    private String contactUser;
    /**
     *
     */
    private String preferredCodecOnly;
    /**
     *
     */
    private String asymmetricRtpCodec;
    /**
     *
     */
    private String rtcpMux;
    /**
     *
     */
    private String allowOverlap;
    /**
     *
     */
    private String referBlindProgress;
    /**
     *
     */
    private String notifyEarlyInuseRinging;
    /**
     *
     */
    private Integer maxAudioStreams;
    /**
     *
     */
    private Integer maxVideoStreams;
    /**
     *
     */
    private String webrtc;
    /**
     *
     */
    private String dtlsFingerprint;
    /**
     *
     */
    private String incomingMwiMailbox;
    /**
     *
     */
    private String bundle;
    /**
     *
     */
    private String dtlsAutoGenerateCert;
    /**
     *
     */
    private String followEarlyMediaFork;
    /**
     *
     */
    private String acceptMultipleSdpAnswers;
    /**
     *
     */
    @Column("suppress_q850_reason_headers")
    private String suppressQ850ReasonHeaders;
    /**
     *
     */
    private String trustConnectedLine;
    /**
     *
     */
    private String sendConnectedLine;
    /**
     *
     */
    @Column("ignore_183_without_sdp")
    private String ignore183WithoutSdp;
    /**
     *
     */
    private String codecPrefsIncomingOffer;
    /**
     *
     */
    private String codecPrefsOutgoingOffer;
    /**
     *
     */
    private String codecPrefsIncomingAnswer;
    /**
     *
     */
    private String codecPrefsOutgoingAnswer;
    /**
     *
     */
    private String stirShaken;
    /**
     *
     */
    private String sendHistoryInfo;
    /**
     *
     */
    private String allowUnauthenticatedOptions;
    /**
     *
     */
    @Column("t38_bind_udptl_to_media_address")
    private String t38BindUdptlToMediaAddress;
    /**
     *
     */
    private String geolocIncomingCallProfile;
    /**
     *
     */
    private String geolocOutgoingCallProfile;
    /**
     *
     */
    private String incomingCallOfferPref;
    /**
     *
     */
    private String outgoingCallOfferPref;
    /**
     *
     */
    private String stirShakenProfile;
    /**
     *
     */
    private String securityNegotiation;
    /**
     * 密钥管理
     */
    private String securityMechanisms;
    /**
     * 发送AOC
     */
    private String sendAoc;
    /**
     * 媒体会话上下文
     */
    private String overlapContext;
    /**
     * 租户ID
     */
    @Column(tenantId = true)
    private String tenantid;
    /**
     *
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean suppressMohOnSendonly;

    /**
     * 外地号码前是否加0
     */
    private Boolean addZero;

    /**
     * 被叫号码前缀
     */
    private String prefix;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}
