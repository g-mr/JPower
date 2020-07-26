package com.wlcb.jpower.module.dbs.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wlcb.jpower.module.base.annotation.Excel;
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

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    @Excel(name = "创建人")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN")
    @Excel(name = "创建时间",width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh_CN")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Integer status;

    @JSONField(serialize = false)
    @TableField(exist = false)
    @JsonIgnore
    private Map<String, Object> params = new HashMap<>();

}
