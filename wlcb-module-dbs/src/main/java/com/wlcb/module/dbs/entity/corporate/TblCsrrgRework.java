package com.wlcb.module.dbs.entity.corporate;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Corporate
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-27 02:28
 * @Version 1.0
 */
@Data
public class TblCsrrgRework implements Serializable {

    private static final long serialVersionUID = 412755129157532112L;

    private String id;
    private String openid;
    private String corporateId;
    private Integer corporatePeopleSum;
    private Integer corporateRepSum;
    private String planPath;
    private String committedPath;
    private Integer reviewStats;
    private String refuseReason;
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    private Integer status;

    //申请人姓名
    private String name;

}
