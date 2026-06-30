package com.qidiangk.smart.aster.dbs.entity.asterisk;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.jpower.core.dbs.support.type.YesOrNoEnumTypeHandler;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.QueueStrategyEnum;

import java.math.BigInteger;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ASTERISK_DATASOURCE;

/**
 *  DO
 *
 * @author mr.g
 */
@Data
@Table(value = "queues", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class QueuesDO extends TenantEntity {

    /**
     * 队列名称
     */
    @Id(keyType = KeyType.None)
    private String name;
    /**
     * 显示名称
     */
    private String showName;
    /**
     * 等待音乐，当呼叫在队列中等待时播放的音乐
     */
    private String musiconhold;
    /**
     * 周期性播报的语音文件。用于告知客户队列情况。
     */
    private String announce;
    /**
     * 上下文，在队列中使用TRANSFER函数时使用的拨号计划上下文
     */
    private String context;
    /**
     * 超时时间，队列中每个成员振铃的时间（秒）
     */
    private Integer timeout;
    /**
     * 	如果坐席分机正忙（但未接听），是否仍然向其振铃。通常设为 no。
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean ringinuse;
    /**
     * 是否设置各种通道变量，用于在拨号计划中传递信息。
     */
    private String setinterfacevar;
    /**
     * 是否设置各种通道变量，用于在拨号计划中传递信息。
     */
    private String setqueuevar;
    /**
     * 设置队列入口变量，为队列中的每个入口设置的变量
     */
    private String setqueueentryvar;
    /**
     * 录音格式，队列录音文件的格式
     */
    private String monitorFormat;
    /**
     * 成员宏，为队列成员执行的宏
     */
    private String membermacro;
    /**
     * 成员Gosub，为队列成员执行的子程序
     */
    private String membergosub;
    /**
     * 通知下一个语音文件，告诉呼叫者他们是下一个被接听的
     */
    private String queueYouarenext;
    /**
     * 存在语音文件，告诉呼叫者有X个呼叫在等待
     */
    private String queueThereare;
    /**
     * 呼叫等待语音文件，告诉呼叫者他们的呼叫正在等待
     */
    private String queueCallswaiting;
    /**
     * 数量1语音文件，单数数量的数字语音文件
     */
    private String queueQuantity1;
    /**
     * 数量2语音文件，复数数量的数字语音文件
     */
    private String queueQuantity2;
    /**
     * 持续时间语音文件，告诉呼叫者预计的保持时间
     */
    private String queueHoldtime;
    /**
     * 分钟复数语音文件，用于复数分钟
     */
    private String queueMinutes;
    /**
     * 分钟单数语音文件，用于单数分钟
     */
    private String queueMinute;
    /**
     * 秒数语音文件，用于播报秒数
     */
    private String queueSeconds;
    /**
     * 感谢语音文件，在队列结束后播放感谢语
     */
    private String queueThankyou;
    /**
     * 呼叫者公告语音文件，向代理通报呼叫者的身份
     */
    private String queueCallerannounce;
    /**
     * 报告保持时间语音文件，报告给定呼叫者的保持时间
     */
    private String queueReporthold;
    /**
     * 公告频率，播放队列信息的频率（秒）
     */
    private Integer announceFrequency;
    /**
     * 向第一个用户公告，是否对第一个排队的用户播放公告
     */
    private String announceToFirstUser;
    /**
     * 最小公告频率，公告的最小间隔时间（秒）
     */
    private Integer minAnnounceFrequency;
    /**
     * 公告舍入秒数，公告中保持时间的舍入单位
     */
    private Integer announceRoundSeconds;
    /**
     * 公告保持时间，是否公告呼叫者的预计保持时间
     */
    private String announceHoldtime;
    /**
     * 公告位置，是否公告呼叫者在队列中的位置
     */
    private String announcePosition;
    /**
     * 公告位置限制，只有当位置小于或等于此值时才公告位置
     */
    private Integer announcePositionLimit;
    /**
     * 定期公告，定期播放的语音文件
     */
    private String periodicAnnounce;
    /**
     * 定期公告频率，定期公告的播放频率（秒）
     */
    private Integer periodicAnnounceFrequency;
    /**
     * 相对定期公告，相对于当前时间播放的定期公告
     */
    private String relativePeriodicAnnounce;
    /**
     * 随机定期公告，随机播放的定期公告
     */
    private String randomPeriodicAnnounce;
    /**
     * 重试次数，当没有成员应答时，重新尝试的次数
     */
    private Integer retry;
    /**
     * 话后处理时间，成员处理完一个呼叫后的清理时间（秒）
     */
    private Integer wrapuptime;
    /**
     * 惩罚成员限制，当成员达到惩罚级别时的限制
     */
    private Integer penaltymemberslimit;
    /**
     * 是否让多个等待呼叫同时预测坐席空闲时间并提前开始振铃
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean autofill;
    /**
     * 监控类型，录音监控的类型
     */
    private String monitorType;
    /**
     * 自动暂停，当成员忙时是否自动暂停
     */
    private String autopause;
    /**
     * 自动暂停延迟，自动暂停前的延迟时间
     */
    private Integer autopausedelay;
    /**
     * 忙碌时自动暂停，当成员忙线时是否自动暂停
     */
    private String autopausebusy;
    /**
     * 不可用时自动暂停，当成员不可用时是否自动暂停
     */
    private String autopauseunavail;
    /**
     * 最大长度，队列中允许的最大呼叫数量
     */
    private Integer maxlen;
    /**
     * 服务水平，计算服务水平统计的时间窗口（秒）
     */
    private Integer servicelevel;
    /**
     * 分配策略，呼叫分配给成员的策略
     */
//    @TableField(typeHandler = MybatisEnumTypeHandler.class)
    private QueueStrategyEnum strategy;
    /**
     * 加入空队列，当队列为空时是否允许加入
     */
    private String joinempty;
    /**
     * 离开当空，当队列变空时呼叫是否离开
     */
    private String leavewhenempty;
    /**
     * 报告保持时间，是否报告保持时间
     */
    private String reportholdtime;
    /**
     * 成员延迟，将呼叫发送到成员之前的延迟时间
     */
    private Integer memberdelay;
    /**
     * 权重，队列成员的权重值
     */
    private Integer weight;
    /**
     * 当呼叫被转移到另一个坐席时，是否重置超时计时器。
     */
    @Column(typeHandler = YesOrNoEnumTypeHandler.class)
    private Boolean timeoutrestart;
    /**
     * 默认规则，队列的默认规则
     */
    private String defaultrule;
    /**
     * 超时优先级，超时处理的优先级
     */
    private String timeoutpriority;
    /**
     * 记录受限主叫标识，是否记录受限制的主叫号码
     */
    private String logRestrictedCallerId;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}
