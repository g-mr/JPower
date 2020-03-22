package com.wlcb.wlj.module.dbs.entity.pay;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TblWecharRed
 * @Description TODO 微信红包记录
 * @Author 郭丁志
 * @Date 2020-03-21 14:46
 * @Version 1.0
 */
@Data
public class TblWecharRed implements Serializable {

    private static final long serialVersionUID = -7541706702093006092L;

    private String id;

    private String orderNum;
    private String openid;
    private String reUserName;
    private String price;
    private String note;
    private String ip;
    private String returnCode;
    private String returnMsg;
    private String resultCode;
    private String errCode;
    private String errCodeDes;
    private String wxPaymentNo;
    private String wxPaymentTime;
    private Integer payType;

    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private Integer status;
}
