package com.wlcb.jpower.module.dbs.entity.base;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Data
public class BaseEntity implements Serializable {

    protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "创建部门",hidden=true)
    @TableField(value = "create_org", fill = FieldFill.INSERT)
    private String createOrg;

    @ApiModelProperty(value = "创建人",hidden=true)
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "创建时间",hidden=true)
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人",hidden=true)
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ApiModelProperty(value = "修改时间",hidden=true)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    @ApiModelProperty(hidden = true)
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    @JsonIgnore
    @JSONField(serialize = false)
    private Boolean isDeleted;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

}
