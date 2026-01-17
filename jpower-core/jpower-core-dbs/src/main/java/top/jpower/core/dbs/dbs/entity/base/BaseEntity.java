package top.jpower.core.dbs.dbs.entity.base;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础Entity
 *
 * @author mr.g
 */
@Data
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建部门
     * 设置onUpdateValue保证更新得时候不会被更新掉
     **/
    @Column(onUpdateValue = "create_org")
    private Long createOrg;

    /**
     * 创建人
     * 设置onUpdateValue保证更新得时候不会被更新掉
     **/
    @Column(onUpdateValue = "create_user")
    private Long createUser;

    /**
     * 创建时间
     * 设置onUpdateValue保证更新得时候不会被更新掉
     **/
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @Column(onUpdateValue = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改人
     **/
    private Long updateUser;

    /**
     * 修改时间
     **/
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;

    /**
     * 删除时间戳
     **/
    @Column(isLogicDelete = true)
    @JsonIgnore
    @JSONField(serialize = false)
    private BigInteger deleteTime;

    /**
     * 扩展参数/字典翻译
     **/
    @Column(ignore = true)
    private Map<String, Object> params = new HashMap<>();

}
