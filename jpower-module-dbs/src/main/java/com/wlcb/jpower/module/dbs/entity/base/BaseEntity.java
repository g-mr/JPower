package com.wlcb.jpower.module.dbs.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wlcb.jpower.module.base.annotation.Excel;
import com.wlcb.jpower.module.common.utils.DateUtil;
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

    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    @Excel(name = "创建人")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @JSONField(format=DateUtil.PATTERN_DATETIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DateUtil.PATTERN_DATETIME,locale = "zh_CN")
    @Excel(name = "创建时间",width = 30, dateFormat = DateUtil.PATTERN_DATETIME, type = Excel.Type.EXPORT)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DateUtil.PATTERN_DATETIME,locale = "zh_CN")
    @JSONField(format= DateUtil.PATTERN_DATETIME)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Excel(name = "状态",readConverterExp="1=正常")
    @TableField(fill = FieldFill.INSERT)
    private Integer status;

//    @TableField(fill = FieldFill.INSERT)
//    @TableLogic
//    private Integer isDeleted;

    @JSONField(serialize = false)
    @TableField(exist = false)
    @JsonIgnore
    private Map<String, Object> params = new HashMap<>();

}
