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
public class TblCsrrgCorporate implements Serializable {
    private static final long serialVersionUID = 4979926483391842795L;

    private String id;
    private String area;
    private String enterpriseName;
    private String legalPerson;
    private String enterpriseRange;
    private String legalIdcard;
    private String enterpriseAuthority;
    private String enterpriseStatus;
    private String organizationCode;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;

}
