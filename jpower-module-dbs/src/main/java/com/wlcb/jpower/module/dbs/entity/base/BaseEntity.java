package com.wlcb.jpower.module.dbs.entity.base;

import com.alibaba.fastjson.annotation.JSONField;
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

    private String id;
    @Excel(name = "创建人")
    private String createUser;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间",width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date createTime;
    private String updateUser;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer status;

    private Map<String, Object> params = new HashMap<>();

}
