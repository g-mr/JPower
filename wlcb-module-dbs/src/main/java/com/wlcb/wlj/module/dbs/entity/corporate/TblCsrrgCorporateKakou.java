package com.wlcb.wlj.module.dbs.entity.corporate;

import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName Corporate
 * @Description TODO 企业卡口关联表
 * @Author 郭丁志
 * @Date 2020-02-27 02:28
 * @Version 1.0
 */
@Data
public class TblCsrrgCorporateKakou implements Serializable {

    private static final long serialVersionUID = -4931820257117467452L;

    private String id;
    private String corporateId;
    private String kakouId;
    private String recordId;
    private String recordOpenid;
    private Integer reviewStatus;
    private String refuseReason;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

    //TODO 查询条件
    private String quxian;


    private String createTimeStr;

}
