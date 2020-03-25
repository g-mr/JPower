package com.wlcb.ylth.module.dbs.entity.tiwen;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TblSupplies
 * @Description TODO 物资分配
 * @Author 郭丁志
 * @Date 2020-03-09 17:57
 * @Version 1.0
 */
@Data
public class TblSupplies implements Serializable {

    private static final long serialVersionUID = -1675836730941682009L;

    private String id;

    private String openid;

    private Integer bayonetNumber;
    private String bayonetName;

    private String supplies;

    private String quxian;
    private String jiedao;
    private String shequ;
    private String xiaoqu;

    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    private Integer status;


    //查询条件
    private String startTime;
    private String endTime;
}
