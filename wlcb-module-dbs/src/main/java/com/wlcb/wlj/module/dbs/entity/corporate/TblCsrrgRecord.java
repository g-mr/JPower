package com.wlcb.wlj.module.dbs.entity.corporate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Corporate
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-27 02:28
 * @Version 1.0
 */
@Data
public class TblCsrrgRecord implements Serializable {

    private static final long serialVersionUID = 6648147019252600177L;

    private String id;
    private String corporateId;
    private String enterpriseName;
    private String applicantOpenid;
    private String applicantName;
    private String applicantIdcard;
    private String applicantUsername;
    private String applicantPhone;
    private String applicantQuxian;
    private String applicantShequ;
    private String applicantJiedao;
    private String applicantXiaoqu;
    private String applicantShequId;
    private String applicantJiedaoId;
    private String applicantXiaoquId;
    private String filePath;
    private Integer applicantStatus;
    private String failReason;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

    private String quxian;

}
