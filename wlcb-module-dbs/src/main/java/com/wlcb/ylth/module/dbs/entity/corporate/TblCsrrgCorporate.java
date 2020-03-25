package com.wlcb.ylth.module.dbs.entity.corporate;

import com.wlcb.ylth.module.dbs.entity.base.BaseEntity;
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
public class TblCsrrgCorporate extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4979926483391842795L;

    private String id;
    private String area;
    private String enterpriseName;
    private String legalPerson;
    private String enterpriseRange;
    private String legalIdcard;
    private String legalPhone;
    private String enterpriseAuthority;
    private String enterpriseStatus;
    private String organizationCode;
    private String quxian;
    private String enterprisePhone;
    private String registeredMoney;
    private String enterpriseType;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

}
