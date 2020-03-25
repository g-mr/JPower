package com.wlcb.ylth.module.dbs.entity.base;

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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

    private static final long serialVersionUID = 1L;

    private String id;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

    private String createTimeStr;
    private String updateTimeStr;

    private Map<String, Object> params = new HashMap<>();

    public String getCreateTimeStr() {

        if (getCreateTime()!=null){
            createTimeStr = sdf.format(getCreateTime());
        }

        return createTimeStr;
    }

    public String getUpdateTimeStr() {

        if (getUpdateTime()!=null){
            createTimeStr = sdf.format(getUpdateTime());
        }

        return updateTimeStr;
    }
}
