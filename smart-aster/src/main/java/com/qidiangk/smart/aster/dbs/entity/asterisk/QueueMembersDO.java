package com.qidiangk.smart.aster.dbs.entity.asterisk;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
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
@Data
@Table(value = "queue_members", dataSource = ASTERISK_DATASOURCE)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueMembersDO extends BaseEntity {

    
    /**
     * 队列名称
     */
    private String queueName;
    
    /**
     * 成员接口标识（如SIP/1001或PJSIP/1001等）
     */
    @Column("interface")
    private String interfaceName;
    
    /**
     * 成员名称（显示名称）
     */
    private String membername;
    
    /**
     * 状态接口
     */
    private String stateInterface;
    
    /**
     * 权重值（优先级，数字越小优先级越高）
     */
    private Integer penalty;
    
    /**
     * 是否暂停（0-未暂停，1-暂停）
     */
//    @TableField(typeHandler = BooleanTypeHandler.class)
    private Boolean paused;
    
    /**
     * 唯一ID
     */
    private Integer uniqueid;
    
    /**
     * 结束通话后等待时间（秒）
     */
    private Integer wrapuptime;
    
    /**
     * 当设备正在使用时是否响铃
     */
    private Boolean ringinuse;
    
    /**
     * 暂停原因
     */
    private String reasonPaused;

    @Column(ignore = true, isLogicDelete = false)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

}