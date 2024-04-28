package top.jpower.jpower.module.dbs.entity.base;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础Entity
 *
 * @author mr.g
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主健
     **/
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建部门
     **/
    @TableField(value = "create_org", fill = FieldFill.INSERT)
    private Long createOrg;

    /**
     * 创建人
     **/
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     **/
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     **/
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     **/
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     **/
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    @JsonIgnore
    @JSONField(serialize = false)
    private Boolean isDeleted;

    /**
     * 扩展参数/字典翻译
     **/
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

}
