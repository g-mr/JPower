package com.wlcb.ylth.module.dbs.entity.corporate;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Corporate
 * @Description TODO 公司法人审核表
 * @Author 郭丁志
 * @Date 2020-02-27 02:28
 * @Version 1.0
 */
@Data
public class TblCsrrgCorporateReview implements Serializable {

    private static final long serialVersionUID = 1399423892618317851L;
    private String id;
    private String registeredNumber;
    private String organizationCode;
    private String enterpriseName;
    private String area;
    private String enterpriseType;
    private String legalPerson;
    private String legalPhone;
    private String liaisonName;
    private String liaisonPhone;
    private String enterpriseAuthority;
    private String enterprisePhone;
    private String registeredMoney;
    private String enterpriseRange;
    private String registeredDate;
    private String legalIdcard;
    private String liaisonIdcard;
    private String authorityFile;
    private Integer reviewStats;
    private String refuseReason;
    private String quxian;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

}
